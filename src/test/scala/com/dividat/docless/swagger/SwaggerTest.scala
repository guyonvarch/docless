package com.dividat.docless.swagger

import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.JsonSchemaFactory
import io.circe._
import io.circe.syntax._
import org.scalatest.freespec.AnyFreeSpec

import scala.collection.JavaConverters._

class SwaggerTest extends AnyFreeSpec {
  "Can build and serialise a swagger object" in {
    val petstoreSchema = PetstoreSchema()
    val json           = JsonLoader.fromResource("/swagger-schema.json")
    val schema         = JsonSchemaFactory.byDefault().getJsonSchema(json)
    val printer        = Printer.spaces2.copy(dropNullValues = true)
    val jsonS          = printer.pretty(petstoreSchema.asJson)
    val report         = schema.validate(JsonLoader.fromString(jsonS))
    val err            = System.err

    if (!report.isSuccess) {
      err.println(jsonS)
      err.println(
        "============== Validation errors ================================"
      )
      val errors = report.asScala.toList
      errors.foreach(err.println)
      fail()
    }
  }
}
