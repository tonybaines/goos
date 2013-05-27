package tonybaines.goos.app

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.packet.Message

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

import static tonybaines.goos.app.Main.*


class FakeAuctionServer {
  static final String XMPP_HOSTNAME = "localhost"
  static final AUCTION_PASSWORD = "auction"
  final String itemId
  final XMPPConnection connection
  private Chat currentChat

  public FakeAuctionServer(String itemId) {
    this.itemId = itemId
    this.connection = new XMPPConnection(XMPP_HOSTNAME)
  }

  public String getItemId() {
    return itemId
  }


  private final SingleMessageListener messageListener = new SingleMessageListener()

  public void startSellingItem() throws XMPPException {
    connection.connect()
    connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE)
    connection.getChatManager().addChatListener(
      new ChatManagerListener() {
        public void chatCreated(Chat chat, boolean createdLocally) {
          currentChat = chat
          chat.addMessageListener(messageListener)
        }
      })
  }

  public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
    messageListener.receivesAMessage()
  }

  public void announceClosed() throws XMPPException {
    currentChat.sendMessage(new Message())
  }

  public void stop() {
    connection.disconnect()
  }

  public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages =
      new ArrayBlockingQueue<Message>(1)

    public void processMessage(Chat chat, Message message) {
      messages.add(message)
    }

    public void receivesAMessage() throws InterruptedException {
      assert messages.poll(5, TimeUnit.SECONDS) != null
    }
  }

}
