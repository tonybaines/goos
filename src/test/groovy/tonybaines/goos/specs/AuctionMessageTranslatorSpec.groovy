package tonybaines.goos.specs

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message
import spock.lang.Specification
import tonybaines.goos.AuctionEventListener
import tonybaines.goos.AuctionMessageTranslator
import tonybaines.goos.testsupport.ApplicationRunner

class AuctionMessageTranslatorSpec extends Specification {
  static final Chat UNUSED_CHAT = null
  final def listener = Mock(AuctionEventListener)
  final def translator = new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, listener)

  def "notifies auction closed when close message received"() {
    when:
    Message message = new Message()
    message.setBody("SOLVersion: 1.1; Event: CLOSE;")
    translator.processMessage(UNUSED_CHAT, message)

    then:
    1 * listener.auctionClosed()
  }

  def "notifies bid details when current price message received from other bidder"() {
    when:
    Message message = new Message()
    message.setBody(
      "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;"
    )
    translator.processMessage(UNUSED_CHAT, message)

    then:
    1 * listener.currentPrice(192, 7, AuctionEventListener.PriceSource.FromOtherBidder)
  }

  def "notifies bid details when current price message received from sniper"() {
    when:
    Message message = new Message()
    message.setBody(
      "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: ${ApplicationRunner.SNIPER_ID};"
    )
    translator.processMessage(UNUSED_CHAT, message)

    then:
    1 * listener.currentPrice(234, 5, AuctionEventListener.PriceSource.FromSniper)
  }
}
