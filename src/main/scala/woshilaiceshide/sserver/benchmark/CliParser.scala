package woshilaiceshide.sserver.benchmark

object CliParser {

  final case class CliOption(name: String, required: Boolean, has_arg: Boolean, help: String) {
    def show_name = if (has_arg) {
      s"-${name}=<arg>"
    } else {
      s"-${name}"
    }
  }

  final case class CliOptions(options: CliOption*) {

    private def show(s: String, pad_to_length: Int, has_arg: Boolean): Unit = {
      Console.err.print(s)
      Console.err.print(Array.fill((pad_to_length - s.length()))(' ').mkString)
    }

    def show(header: String = "", footer: String = ""): Unit = {

      val pad_to_length = options.map { _.show_name.length() + 3 }.max
      Console.err.println(header)
      options.map { o =>
        show(o.show_name, pad_to_length, o.has_arg)
        if (o.required) {
          Console.err.print("    required   ")
        } else {
          Console.err.print("not required   ")
        }
        Console.err.println(o.help)
      }
      Console.err.println(footer)
    }
  }

  object CliOptions {

  }

  private val P1 = """\-([a-zA-Z0-9\.]+)""".r
  private val P2 = """\-([a-zA-Z0-9\.]+)=([a-zA-Z0-9\.]*)""".r

  abstract class CliParseException(info: String) extends Exception(info)
  final case class BadFormatParseException(info: String) extends CliParseException(info)
  final case class UnknownOptionParseException(info: String) extends CliParseException(info)
  final case class NoRequiredOptionParseException(info: String) extends CliParseException(info)

  private def parse_an_arg(options: CliOptions, arg: String, ignore_unknown: Boolean = true, ignore_bad_format: Boolean = true) = {

    arg match {
      case P1(k) => {
        if (!options.options.exists { _.name == k }) {
          throw new UnknownOptionParseException(s"${arg} is an unknown option.")
        }
        Some(k -> "<null>")
      }
      case P2(k, v) => {
        if (!ignore_unknown && !options.options.exists { _.name == k }) {
          throw new UnknownOptionParseException(s"${arg} is an unknown option.")
        }
        Some(k -> v)
      }
      case _ => {
        if (!ignore_bad_format) {
          throw new BadFormatParseException(s"${arg} is in bad format. the good one should be -xxx=yyy.")
        } else {
          None
        }
      }
    }

  }

  def parse(options: CliOptions, args: Array[String], ignore_unknown: Boolean = true, ignore_bad_format: Boolean = true) = {
    //TODO duplicated???
    val tmp = args.map { arg => parse_an_arg(options, arg, ignore_unknown, ignore_bad_format) }
      .filter(_ != None)
      .map { _.get }
      .toMap

    val no = options.options.filter { option => option.required && !tmp.contains(option.name) }
    if (no.size > 0) {
      throw new NoRequiredOptionParseException(s"${no.map { _.show_name }.mkString(", ")} is required, but lost.")
    }

    tmp

  }

  def test() {
    val option_s = CliOption("s", true, true, "which server to run? akka? jetty? netty? rapidoid? undertow? sserver?")
    val option_i = CliOption("i", false, true, "listen on this interface, default to 0.0.0.0")
    val option_p = CliOption("p", false, true, "listen on this port, default to 8383")

    val options = CliOptions(option_s, option_i, option_p)

    options.show("cli test")

    val parsed = parse(options, Array("-s=sserver", "-x=y"))
    println(s"parsed: ${parsed}")
  }

}