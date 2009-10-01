package com.cve.fs;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import java.io.PrintStream;

public final class Exec2 {
  
  public static void main(String[] arg) throws JSchException, IOException, InterruptedException {
	  UserInfo ui = new UserInfoValue(null,"password");
	  exec("curt@127.0.0.1","ls /home/curt",ui);
  }

	static void exec(final String spec, final String command, final UserInfo ui) throws JSchException, IOException, InterruptedException{
	      JSch jsch=new JSch();  

	      String user=spec.substring(0, spec.indexOf('@'));
	      String host=spec.substring(spec.indexOf('@')+1);
	      Session session=jsch.getSession(user, host, 22);
	      session.setUserInfo(ui);
	      session.connect();
	      Channel channel=session.openChannel("exec");
          ChannelExec exec = (ChannelExec)channel;
	      exec.setCommand(command);

	      channel.setInputStream(null);
	      exec.setErrStream(System.err);

	      InputStream in=channel.getInputStream();
	      channel.connect();
          run(channel,in,System.out);
	      channel.disconnect();
	      session.disconnect();
	  }

    static void run(Channel channel, InputStream in, PrintStream out) throws IOException, InterruptedException {
        byte[] tmp=new byte[1024];
        while(true){
            while(in.available()>0){
                int i=in.read(tmp, 0, 1024);
                if(i<0)break;
                out.print(new String(tmp, 0, i));
            }
            if(channel.isClosed()){
                out.println("exit-status: "+channel.getExitStatus());
                break;
            }
            Thread.sleep(1000);
        }
    }
}
