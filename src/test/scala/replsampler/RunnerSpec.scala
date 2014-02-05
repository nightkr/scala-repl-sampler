package replsampler

class RunnerSpec extends UnitSpec {
  "A Runner" - {
    "when empty" - {
      "and bufInterpret is called with an empty string" - {
        "should produce an empty success string" in {
          val r = new Runner
          inside(r.bufInterpret("")) {
            case Some(Runner.Result(_, output, result)) =>
              output should be("")
              result should be(Runner.Success)
          }
        }
      }
    }

    "when defining a class" - {
      "it should be possible to instantiate the class" in {
        val r = new Runner
        inside(r.bufInterpret("class MyClass")) {
          case Some(Runner.Result(_, _, result)) =>
            result should be(Runner.Success)
        }
        inside(r.bufInterpret("new MyClass")) {
          case Some(Runner.Result(_, output, result)) =>
            output should startWith("res0: MyClass = MyClass@")
            result should be(Runner.Success)
        }
      }
    }
  }
}
