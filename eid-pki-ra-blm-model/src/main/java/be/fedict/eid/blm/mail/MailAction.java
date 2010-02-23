package be.fedict.eid.blm.mail;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;

@Stateless
public class MailAction {
	
	@Logger
	private Log log;
	
	@In(create=true)
	private Renderer renderer;	
	
	public void send(){
		try{
 			renderer.render("/view/template/mail-template.xhtml");
		}catch(Exception e){

		}
	}	
}
