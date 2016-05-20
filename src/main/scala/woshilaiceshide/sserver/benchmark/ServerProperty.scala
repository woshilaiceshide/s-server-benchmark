package woshilaiceshide.sserver.benchmark

import CliParser._

object ServerProperty {

  val option_s = CliOption("s", false, true, "which server to run? akka? jetty? netty? rapidoid? undertow? sserver?")
  val option_i = CliOption("i", false, true, "listen on this interface, default to 0.0.0.0")
  val option_p = CliOption("p", false, true, "listen on this port, default to 8383")

  val options = CliOptions(option_s, option_i, option_p)

  def print_help() = {
    options.show("s-server-benchmark")
  }

}

trait ServerProperty { this: App =>

  private def parsed = CliParser.parse(ServerProperty.options, args, true, false)

  def interface = {
    parsed.get("i").getOrElse("0.0.0.0")
  }

  def port = {
    parsed.get("p").getOrElse("8383").toInt
  }

  def chosen_server = {
    parsed.get("s")
  }

}