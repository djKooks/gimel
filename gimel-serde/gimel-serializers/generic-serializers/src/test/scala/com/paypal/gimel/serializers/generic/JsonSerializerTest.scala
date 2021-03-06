/*
 * Copyright 2018 PayPal Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paypal.gimel.serializers.generic

import org.scalatest._

import com.paypal.gimel.serde.common.spark.SharedSparkSession

class JsonSerializerTest extends FunSpec with Matchers with SharedSparkSession {

  val jsonSerializer = new JsonSerializer()

  describe ("serialize") {
    it("it should return a serialized dataframe") {
      val dataFrame = mockDataInDataFrame(10)
      val serializedDF = jsonSerializer.serialize(dataFrame)
      val deserializedRDD = serializedDF.rdd.map { eachRow => eachRow.getAs("value").asInstanceOf[String] }
      val deserializedDF = spark.read.json(deserializedRDD)
      assert(deserializedDF.columns.sorted.sameElements(dataFrame.columns.sorted))
      assert(deserializedDF.except(dataFrame).count() == 0)
    }
  }
}
