package com.urbanski.wordcounter.model

import io.circe.parser._
import org.scalatest.EitherValues._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class EventJsonFormatTest extends AnyWordSpec {

  "Event format" should {
    "parse correct JSON" in {
      val json         = """{ "event_type": "baz", "data": "amet", "timestamp": 1639866020 }"""
      val decodeResult = decode[Event](json)
      decodeResult.value shouldBe Event(EventType("baz"), Data("amet"), Timestamp(1639866020))
    }

    "fail if the event type fields is not in a snake case format" in {
      val json         = """{ "eventType": "baz", "data": "amet", "timestamp": 1639866020 }"""
      val decodeResult = decode[Event](json)
      decodeResult.left.value
    }

    "expect all fields to be present" in {
      val json         = """{ "data": "amet", "timestamp": 1639866020 }"""
      val decodeResult = decode[Event](json)
      decodeResult.left.value
    }
  }
}
