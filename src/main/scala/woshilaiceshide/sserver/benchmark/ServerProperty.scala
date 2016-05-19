package woshilaiceshide.sserver.benchmark

import org.apache.commons.cli._

object ServerProperty {

  val options = new Options()
  options.addOption("s", "server", true, "which server to run? akka? jetty? netty? rapidoid? undertow? sserver?")
  options.addOption("i", "interface", true, "listen on this interface, default to 0.0.0.0")
  options.addOption("p", "port", true, "listen on this port, default to 8383")

  def print_help() = {
    val formatter = new HelpFormatter()
    formatter.printHelp("s-server-benchmark", options)
  }

}

trait ServerProperty { this: App =>

  private val parser = new DefaultParser()
  private def cmd = parser.parse(ServerProperty.options, args)

  def interface = {
    if (cmd.hasOption("i")) {
      cmd.getOptionValue("i")
    } else {
      "0.0.0.0"
    }
  }

  def port = {
    if (cmd.hasOption("p")) {
      cmd.getOptionValue("p").toInt
    } else {
      8383
    }
  }

  def chosen_server = {
    if (cmd.hasOption("s")) {
      Some(cmd.getOptionValue("s"))
    } else {
      None
    }
  }

}