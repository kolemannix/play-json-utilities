package com.kolemannix.playjson

import scala.collection.immutable.Seq
import scala.meta._

object MacroHelpers {
  def isCaseClass(cls: Defn.Class): Boolean = cls.mods.exists(_.structure == Mod.Case().structure)
}

class AutoJson(flatten: Boolean = true) extends scala.annotation.StaticAnnotation {

  inline def apply(defn: Any): Any = meta {
    val flatten = this match {
      case q"new AutoJson(${Lit.Boolean(bool)})" => bool
      case q"new AutoJson(flatten = ${Lit.Boolean(b)})" => b
      case _ => true
    }

    defn match {
      case Term.Block(Seq(
      cls @ Defn.Class(mods, name, Nil, Ctor.Primary(Nil, _, fields :: _), _),
      companion: Defn.Object
      )) if MacroHelpers.isCaseClass(cls) && fields.size > 0 =>
        val enhancedCompanion = AutoJson.companionWithFormat(name, Some(companion), fields, flatten)
        Term.Block(Seq(cls, enhancedCompanion))
      case cls @ Defn.Class(mods, name, Nil, Ctor.Primary(Nil, _, fields :: _), _) if MacroHelpers.isCaseClass(cls) && fields.size > 0 =>
        val enhancedCompanion = AutoJson.companionWithFormat(name, None, fields, flatten)
        Term.Block(Seq(cls, enhancedCompanion))
      case bad => abort("Must annotate a case class with no type parameters and at least one field")
    }
  }
}

object AutoJson {

  def companionWithFormat(name: Type.Name, maybeCompanion: Option[Defn.Object], fields: Seq[Term.Param], flatten: Boolean): Defn.Object = {
    val format = fields match {
      case field :: Nil if flatten =>
        val termName = Term.Name(name.value)
        val Term.Param(_, _, Some(typeArg: Type.Arg), _) = field
        q"""
           implicit val format: play.api.libs.json.Format[$name] = play.api.libs.json.Format[$name](
             play.api.libs.json.Reads(_.validate[${Type.Name(typeArg.syntax)}].map($termName.apply)),
             play.api.libs.json.Writes(o => play.api.libs.json.Json.toJson(o.${Term.Name(field.name.value)}))
           )
         """
      case other => q"implicit val format: play.api.libs.json.Format[$name] = play.api.libs.json.Json.format[$name]"
    }
    maybeCompanion match {
      case Some(obj @ Defn.Object(mods, name, Template(early, parents, self, stats))) =>
        obj.copy(templ = obj.templ.copy(stats = Some(stats.getOrElse(Nil) :+ format)))
      case None => q"object ${Term.Name(name.value)} { $format }"
    }
  }
}
