package pure_mp3;

/**
 * Write a description of class Info here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
public class Info extends JPanel
{
   private static final long serialVersionUID = 2385007980763532219L;
   private final JTextField artist_l;
   private final JTextField artist_r;
   private final JTextField title_l;
   private final JTextField title_r;
   private final JTextField album_l;
   private final JTextField album_r;
   private final JTextField length_l;
   private final JTextField length_r;
   private String length;
   
   public Info()
   {
       super();
//       setBackground(Color.WHITE);
//       setLayout(new MigLayout("","[][fill,grow]",""));  
       setLayout(new MigLayout("nogrid, nocache"));
       Global.setInfo(this);
       
       artist_l = new JTextField("Artist:");
       artist_l.setEditable(false);
       add(artist_l, "id artist_l, x 5, y 0, sizegroup l");
       
       artist_r = new JTextField("");
       artist_r.setEditable(false);
       add(artist_r,"id artist_r, pos (artist_l.x2 + 5) artist_l.y 100% artist_l.y2");
       
       title_l = new JTextField("Title:");
       title_l.setEditable(false);
       add(title_l, "id title_l, x 5 , y (artist_l.y2 - 1), sizegroup l");
       
       title_r = new JTextField("");
       title_r.setEditable(false);
       add(title_r,"id title_r,pos (title_l.x2 + 5) title_l.y 100% title_l.y2");
       
       album_l = new JTextField("Album:");
       album_l.setEditable(false);
       add(album_l, "id album_l, x 5 , y (title_l.y2 - 1), sizegroup l");
       
       album_r = new JTextField("");
       album_r.setEditable(false);
       add(album_r,"id album_r, pos (album_l.x2 + 5) album_l.y 100% album_l.y2");
       
       length_l = new JTextField("Length:");
       length_l.setEditable(false);
       add(length_l, "id length_l, x 5 , y (album_l.y2 - 1), sizegroup l");
       
       length_r = new JTextField("");
       length_r.setEditable(false);
       add(length_r,"id length_r, pos (length_l.x2 + 5) length_l.y 100% length_l.y2");
       
       setDropTarget(new DropTarget(this,new PlayListDropTargetListener()));
   }
   
   public void update()
   {
       artist_r.setText(Global.playList.getArtist());
       artist_r.setCaretPosition(0);
       title_r.setText(Global.playList.getTitle());
       title_r.setCaretPosition(0);
       album_r.setText(Global.playList.getAlbum());
       album_r.setCaretPosition(0);
//       length_r.setText(Global.playList.getLength());
//       length_r.setCaretPosition(0);
       length = Global.playList.getLength();
       updatePlayedTime(0);
   }
   
   public void updatePlayedTime(int seconds)
   {
//	   String parts[] = text.split("/");
	   int minutes = seconds / 60;
	   String seconds_ = "" + seconds % 60;
	   if(seconds_.length() < 2)
	   {
		   seconds_ = "0" + seconds_;
	   }
	   if(length.length() > 0)
	   {
		   length_r.setText(minutes + ":" + seconds_ + "/" + length);
	   }
	   else
	   {
		   length_r.setText("");
	   }
	   length_r.setCaretPosition(0);
   }
   
}
