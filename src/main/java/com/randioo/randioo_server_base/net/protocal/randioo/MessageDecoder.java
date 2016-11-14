package com.randioo.randioo_server_base.net.protocal.randioo;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.randioo.randioo_server_base.error.NumberFakeException;


public class MessageDecoder extends CumulativeProtocolDecoder{
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
		    throws Exception
		  {
		    in.order(ByteOrder.LITTLE_ENDIAN);

		    int remaining = in.remaining();
		    if (remaining >= 6)
		    {
		      in.mark();

		      int size = in.getInt();
		      if (size > 2048) {
		        throw new NumberFakeException("msg size too long");
		      }

		      if (size > in.remaining()) {
		        in.reset();
		        return false;
		      }
		      Message message = new Message();

		      short actionNum = in.getShort();
		      message.setType(actionNum);
		      byte[] data = new byte[size - 2];

		      in.get(data);

		      IoBuffer params = IoBuffer.wrap(data);
		      params.order(ByteOrder.LITTLE_ENDIAN);
		      message.setData(params);
		      out.write(message);

		      if (in.remaining() > 0) {
		        return true;
		      }

		    }

		    return false;
		  }
}
