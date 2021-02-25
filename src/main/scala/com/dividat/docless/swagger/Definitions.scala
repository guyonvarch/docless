package com.dividat.docless.swagger

import com.dividat.docless.schema.JsonSchema

case class Definitions(get: JsonSchema.Definition*)
object Definitions {
  val empty = Definitions()
}
