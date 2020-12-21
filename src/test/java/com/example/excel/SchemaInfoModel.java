package com.example.excel;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchemaInfoModel {

  private String schemaFile;
  private Boolean validateOnlySchema;
  private Boolean validateFullResponse;
  private String[] jsonIgnore;

  public String getSchemaFile() {
    return schemaFile;
  }

  public void setSchemaFile(String schemaFile) {
    this.schemaFile = schemaFile;
  }

  public Boolean getValidateOnlySchema() {
    return validateOnlySchema;
  }

  public void setValidateOnlySchema(Boolean validateOnlySchema) {
    this.validateOnlySchema = validateOnlySchema;
  }

  public Boolean getValidateFullResponse() {
    return validateFullResponse;
  }

  public void setValidateFullResponse(Boolean validateFullResponse) {
    this.validateFullResponse = validateFullResponse;
  }

  public String[] getJsonIgnore() {
    return jsonIgnore;
  }

  public void setJsonIgnore(String[] jsonIgnore) {
    this.jsonIgnore = jsonIgnore;
  }

  @Override
  public String toString() {
    return "SchemaInfoModel{" +
        "schemaFile='" + schemaFile + '\'' +
        ", validateOnlySchema=" + validateOnlySchema +
        ", validateFullResponse=" + validateFullResponse +
        ", jsonPathExpressionsArray=" + Arrays.toString(jsonIgnore) +
        '}';
  }
}
