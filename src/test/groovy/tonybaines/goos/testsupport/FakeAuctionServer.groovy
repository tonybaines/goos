package tonybaines.goos.testsupport

import groovy.util.logging.Log
import org.hamcrest.Matcher
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.packet.Message
import tonybaines.goos.app.Main

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

// aliased to avoid a clash with groovy.lang.is
import static tonybaines.goos.app.Main.*

@Log
class FakeAuctionServer {
  public static final String XMPP_HOSTNAME = "127.0.0.1"
  private static final AUCTION_PASSWORD = "auction"
  final String itemId
  private final XMPPConnection connection
  private Chat currentChat
  private final SingleMessageListener messageListener = new SingleMessageListener()

  public FakeAuctionServer(String itemId) {
    this.itemId = itemId
    this.connection = new XMPPConnection(XMPP_HOSTNAME)
  }

  public void startSellingItem() throws XMPPException {
    use(XMPPConnectionHelper) { // Scoped mix-in to make the connection setup more literate
      connection.connectAs(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE)
      connection.registerListener { chat, createdLocally ->
        currentChat = chat
        chat.addMessageListener(messageListener)
      }
    }
  }

  public void hasReceivedJoinRequestFrom(bidderId) throws InterruptedException {
    messageListener.receivesAMessageMatching(bidderId, equalTo(Main.JOIN_COMMAND_FORMAT))
  }

  public void announceClosed() throws XMPPException {
    currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;")

  }

  public void stop() {
    connection.disconnect()
  }

  def reportPrice(int currentBid, int minIncrement, String winningBidderId) {
    currentChat.sendMessage(String.format("SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;", currentBid, minIncrement, winningBidderId))
  }

  def hasReceivedBid(int currentBid, String bidderId) throws InterruptedException {
    assert currentChat.getParticipant() == bidderId
    messageListener.receivesAMessageMatching(bidderId, equalTo(String.format(Main.BID_COMMAND_FORMAT, currentBid)))
  }






  public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1)

    public void processMessage(Chat chat, Message message) {
      messages.add(message)
    }

    public void receivesAMessageMatching(String bidderId, Matcher<? super String> messageMatcher) throws InterruptedException {
      def message = messages.poll(5, TimeUnit.SECONDS)
      assert message != null
      assert currentChat.participant == bidderId
      assertThat(message.body, messageMatcher)
    }
  }

  @Category(XMPPConnection)
  public static class XMPPConnectionHelper {
    static void connectAs(XMPPConnection self, username, password, resource) {
      self.connect()
      self.login(username, password, resource)
    }

    static void registerListener(XMPPConnection self, Closure c) {
      self.getChatManager().addChatListener(
        new ChatManagerListener() {
          public void chatCreated(Chat chat, boolean createdLocally) {
            c.call(chat, createdLocally)
          }
        })
    }
  }

}
