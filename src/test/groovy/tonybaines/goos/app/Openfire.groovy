package tonybaines.goos.app

import org.jivesoftware.openfire.XMPPServer

class Openfire {
  XMPPServer openfireXmpp

  def start() {
    System.setProperty("openfireHome", "/home/tony/workspace/goos/openfire")
    openfireXmpp = new XMPPServer()

    waitForTheAdminPlugin()

  }

  private void waitForTheAdminPlugin() {
    def adminPluginReady = false
    while (!adminPluginReady) {
      try {
        "http://localhost:9090".toURL().text
        adminPluginReady = true
      } catch (Exception e) {
        println "Waiting for the admin plugin to initialise"
        sleep 1000
      }
    }
  }

  def stop() {
    openfireXmpp.stop()
  }

  public static void main(String[] args) {
    new Openfire().start()
    while (true) sleep(1000)
  }
}
