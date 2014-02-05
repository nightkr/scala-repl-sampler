package replsampler

class RunnerSpec extends UnitSpec {
  "A Runner" - {
    "when empty" - {
      "and bufInterpret is called with an empty string" - {
        "should produce an empty success string" in {
          val r = new Runner
          assert(r.bufInterpret("") === Some(Runner.Result("", "", Runner.Success)))
        }
      }
    }
  }
}
