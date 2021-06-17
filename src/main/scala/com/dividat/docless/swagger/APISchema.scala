package com.dividat.docless.swagger

import scala.collection.immutable.{ListMap, SortedMap}
import com.dividat.docless.schema.JsonSchema
import com.dividat.docless.schema.JsonSchema.{ArrayRef, TypeRef}
import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._

case class APISchema(
    info: Info,
    host: String,
    basePath: String,
    swagger: String = "2.0",
    paths: Paths = Paths(Nil),
    parameters: OperationParameters = OperationParameters(Nil),
    schemes: Set[Scheme] = Set.empty,
    consumes: Set[String] = Set.empty,
    produces: Set[String] = Set.empty,
    definitions: Definitions = Definitions.empty,
    securityDefinitions: SecurityDefinitions = SecurityDefinitions.empty
) extends ParamSetters[APISchema] {

  def withPaths(ps: Path*): APISchema =
    copy(paths = Paths(ps))

  def defining(ds: JsonSchema.Definition*) =
    copy(definitions = Definitions(ds: _*))

  override def withParams(param: OperationParameter*): APISchema =
    copy(parameters = OperationParameters(param))
}

object APISchema {

  implicit val externalDocsEncoder: Encoder[ExternalDocs] =
    deriveEncoder[ExternalDocs]
  implicit val contactEncoder: Encoder[Info.Contact] =
    deriveEncoder[Info.Contact]
  implicit val licenseEncoder: Encoder[Info.License] =
    deriveEncoder[Info.License]
  implicit val infoEncoder: Encoder[Info] = deriveEncoder[Info]
  implicit val externalDocEnc             = deriveEncoder[ExternalDocs]

  implicit val securitySchemeEncoder = Encoder.instance[SecurityScheme] { s =>
    val common = Map(
      "name"        -> s.name.asJson,
      "description" -> s.description.asJson
    )

    val other = s match {
      case Basic(_, _) =>
        Map("type" -> "basic".asJson)
      case ApiKey(_, in, _) =>
        Map("type" -> "api_key".asJson, "in" -> in.asJson)
      case OAuth2(_, flow, authUrl, tokenUrl, scopes, _) =>
        Map(
          "type"             -> "oauth2".asJson,
          "flow"             -> flow.asJson,
          "authorizationUrl" -> authUrl.asJson,
          "tokenUrl"         -> tokenUrl.asJson,
          "scopes"           -> scopes.asJson
        )
    }
    Json.fromFields(common ++ other)
  }

  implicit val schemaRefEnc = Encoder.instance[JsonSchema.Ref] {
    case ArrayRef(id, _) =>
      Json.obj(
        "type" -> Json.fromString("array"),
        "items" -> Json.obj(
          "$ref" -> Json.fromString(s"#/definitions/$id")
        )
      )
    case TypeRef(id, _) =>
      Json.obj("$ref" -> Json.fromString(s"#/definitions/$id"))
  }

  implicit val operationParameterEnc: Encoder[OperationParameter] =
    Encoder.instance[OperationParameter] { p =>
      val common = Map(
        "name"        -> p.name.asJson,
        "required"    -> p.required.asJson,
        "description" -> p.description.asJson
      )
      val other = p match {
        case BodyParameter(_, _, _, schema) =>
          Map("schema" -> schema.asJson, "in" -> "body".asJson)
        case Parameter(_, _, in, _, typ, format) =>
          Map(
            "in"     -> in.asJson,
            "type"   -> typ.asJson,
            "format" -> format.asJson
          )
        case ArrayParameter(_, _, in, _, itemType, cFormat, minMax, format) =>
          Map(
            "in"               -> in.asJson,
            "type"             -> "array".asJson,
            "items"            -> Json.obj("type" -> itemType.asJson),
            "collectionFormat" -> cFormat.asJson,
            "format"           -> format.asJson
          )
      }
      Json.fromFields(common ++ other)
    }

  implicit val definitionsEnc = Encoder.instance[Definitions] { defs =>
    // Sort definitions alphabetically
    SortedMap.from(defs.get.map(d => d.id -> d.json)).asJson
  }

  implicit val securityDefinitionsEnc = Encoder.instance[SecurityDefinitions] { defs =>
    // Preserve order
    ListMap.from(defs.get.map(d => d.name -> d.asJson)).asJson
  }

  implicit val securityRequirementEncoder = Encoder.instance[SecurityRequirement] { s =>
    Json.fromFields(Map(s.name -> s.scope.asJson))
  }

  implicit val headerEnc   = deriveEncoder[Responses.Header]
  implicit val responseEnc = deriveEncoder[Responses.Response]
  implicit val responsesEnc = Encoder.instance[Responses] { rs =>
    rs.byStatusCode.map { case (code, resp) => code -> resp.asJson }.asJson
  }
  implicit val opParamsEnc = Encoder.instance[OperationParameters] { params =>
    // Preserve order
    ListMap.from(params.get.map(p => p.name -> p.asJson)).asJson
  }
  implicit val operationEnc =
    deriveEncoder[Operation].mapJsonObject(_.remove("id"))

  implicit val pathEnc = Encoder.instance[Path] { p =>
    val obj = JsonObject.singleton("parameters", p.parameters.asJson)
    p.operations
      .foldRight(obj) { // Preserve order
        case ((method, op), acc) =>
          acc.+:(method.entryName -> op.asJson)
      }
      .asJson
  }

  implicit val pathsEnc = Encoder.instance[Paths] { paths =>
    // Preserve order
    ListMap.from(paths.get.map(d => d.id -> d.asJson)).asJson
  }

  implicit val apiSchema = deriveEncoder[APISchema]
}
