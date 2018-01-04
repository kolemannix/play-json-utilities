val githubUrl = url("https://github.com/kolemannix/play-json-utilities")
licenses := Seq("Mit-License" -> url("https://opensource.org/licenses/MIT"))
scmInfo := Some(
  ScmInfo(
    githubUrl,
    "scm:git@github.com:kolemannix/play-json-utilities.git"
  )
)
homepage := Some(githubUrl)
developers := List(
  Developer(
    id    = "kolemannix",
    name  = "Koleman Nix",
    email = "kolemannix@gmail.com",
    url   = url("https://github.com/kolemannix")
  )
)