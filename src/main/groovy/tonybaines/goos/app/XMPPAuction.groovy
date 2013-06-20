package tonybaines.goos.app

import org.jivesoftware.smack.Chat
import tonybaines.goos.Auction

class XMPPAuction implements Auction {
  public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;"
  public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;"
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