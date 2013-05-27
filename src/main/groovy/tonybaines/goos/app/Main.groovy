package tonybaines.goos.app

import groovy.transform.CompileStatic
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.packet.Message

import javax.swing.*

class Main {
  @SuppressWarnings("unused")
  private Chat notToBeGCd

  static final ARG_HOSTNAME = 0
  static final ARG_USERNAME = 1
  static final ARG_PASSWORD = 2
  static final ARG_ITEM_ID = 3

  static final String AUCTION_RESOURCE = "Auction"
  static final String ITEM_ID_AS_LOGIN = "auction-%s"
  static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE


  MainWindow ui

  public Main() throws Exception {
    SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        ui = new MainWindow();
      }
    })
  }


  public static void main(String... args) throws Exception {
    Main main = new Main()
    main.joinAuction(connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]), args[ARG_ITEM_ID])
  }

  private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
    final Chat chat = connection.getChatManager().createChat(
      auctionId(itemId, connection),
      new MessageListener() {
        public void processMessage(Chat aChat, Message message) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              getUi().showStatus(MainWindow.STATUS_LOST) // Access with a getter to bypass odd runtime failure to resolve property
            }
          })
        }
      })
    this.notToBeGCd = chat
    chat.sendMessage(new Message())
  }


  private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
    XMPPConnection connection = new XMPPConnection(hostname)
    connection.connect()
    connection.login(username, password, AUCTION_RESOURCE)
    return connection
  }

  private static String auctionId(String itemId, XMPPConnection connection) {
    return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName())
  }





}

