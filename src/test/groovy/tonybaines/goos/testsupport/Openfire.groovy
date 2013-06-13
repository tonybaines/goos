package tonybaines.goos.testsupport

import groovy.util.logging.Log
import org.jivesoftware.openfire.XMPPServer

@Log
class Openfire {
  XMPPServer openfireXmpp
  final def requiredUsers

  Openfire(users = []) {
    this.requiredUsers = users
  }

  def start() {
    log.info "Openfire XMPP: Starting up"
    def pwd = new File('.').absolutePath
    System.setProperty("openfireHome", "${pwd}/openfire")
    openfireXmpp = new XMPPServer()

    waitForTheAdminPlugin()

    recreateRequiredUsers()

    log.info "Started"
  }

  private void recreateRequiredUsers() {
    def userMgr = openfireXmpp.userManager
    requiredUsers.each { user ->
      userMgr.with {
        deleteUser(getUser(user.name))
        createUser(user.name, user.pass, '', '')
      }
    }
  }

  def stop() {
    log.info "Openfire XMPP: Shutting down"
    openfireXmpp?.stop()
    log.info "Openfire XMPP: Shut down"
  }

  private static void waitForTheAdminPlugin() {
    if(!(1..5).any {
      try {
        "http://localhost:9090".toURL().text
        return true
      } catch (Exception ignored) {
        log.info "Openfire XMPP: Waiting for the admin plugin to initialise"
        sleep 1000
        return false
      }
    }) throw new IllegalStateException("Failed to start Openfire XMPP")
  }

  public static void main(String[] args) {
    new Openfire().start()
    //noinspection GroovyInfiniteLoopStatement
    while (true) sleep(1000)
  }
}
