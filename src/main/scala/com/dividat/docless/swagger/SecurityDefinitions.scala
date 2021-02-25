package com.dividat.docless.swagger

case class SecurityDefinitions(get: SecurityScheme*)
object SecurityDefinitions {
  val empty = SecurityDefinitions()
}
