package com.dividat.docless.swagger

import com.dividat.docless.schema.JsonSchema.{Definition, Ref}

object RefWithContext {
  trait PathContext {
    def path: String
  }

  sealed trait Context
  case class DefinitionContext(definition: Definition) extends Context
  case class ParamContext(param: String, path: String)
      extends Context
      with PathContext
  case class ResponseContext(method: Method, path: String)
      extends Context
      with PathContext

  def definition(ref: Ref, d: Definition) =
    RefWithContext(ref, DefinitionContext(d))
  def param(ref: Ref, param: String, path: String) =
    RefWithContext(ref, ParamContext(param, path))
  def response(ref: Ref, method: Method, path: String) =
    RefWithContext(ref, ResponseContext(method, path))
}

import RefWithContext.Context
case class RefWithContext(ref: Ref, context: Context)
