package com.dividat.docless.swagger

import com.dividat.docless.schema.JsonSchema

trait HasSchema {
  def schema: Option[JsonSchema.Ref]
}
