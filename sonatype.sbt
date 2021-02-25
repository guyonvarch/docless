sonatypeProfileName := "com.dividat"

credentials += Credentials("Sonatype Nexus Repository Manager",
        "oss.sonatype.org",
        sys.env.get("SONATYPE_USERNAME").getOrElse("dividat"),
        sys.env.get("SONATYPE_PASSWORD").getOrElse("")) // ignored by non-publish tasks

publishMavenStyle := true

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/dividat/docless"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/dividat/docless"),
    "scm:git@github.com:dividat/docless.git"
  )
)

developers := List(
  Developer(id="afiore", name="Andrea Fiore", email="", url=url("https://github.com/afiore")),
  Developer(id="etaque", name="Emilien Taque", email="", url=url("https://github.com/etaque")),
  Developer(id="guyonvarch", name="Joris Guyonvarch", email="", url=url("https://github.com/guyonvarch"))
)

