package tonybaines.goos.app

import groovy.swing.SwingBuilder
import groovy.util.logging.Log
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import tonybaines.goos.Auction
import tonybaines.goos.AuctionMessageTranslator
import tonybaines.goos.AuctionSniper
import tonybaines.goos.SniperListener

import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

@Log
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
  static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;"
  static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;"


  static MainWindow ui

  public Main() throws Exception {
    ui = new MainWindow()
  }


  public static void main(String... args) throws Exception {
    Main main = new Main()
    def connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD])
    ui.onClose {
      connection.disconnect()
    }
    main.joinAuction(connection, args[ARG_ITEM_ID])
  }

  private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
    final Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null)
    this.notToBeGCd = chat

    Auction auction = new XMPPAuction(chat)
    chat.addMessageListener(new AuctionMessageTranslator(new AuctionSniper(auction, new SniperStateDisplayer(ui))))
    auction.join()
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

  static class XMPPAuction implements Auction {
    private final Chat chat

    XMPPAuction(Chat chat) {
      this.chat = chat
    }

    @Override
    void bid(int newPrice) {
      chat.sendMessage(String.format(BID_COMMAND_FORMAT, newPrice))
    }

    @Override
    void join() {
      chat.sendMessage(JOIN_COMMAND_FORMAT)
    }
  }
}

