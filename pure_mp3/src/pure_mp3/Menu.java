package pure_mp3;

/**
 * Write a description of class Menu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
public class Menu extends JMenuBar
{
	private static final long serialVersionUID = 2385007980763532219L;
	private JMenu file;
    private JMenuItem fileAddFilePlayList;
    private JMenuItem fileAddFolderPlayList;
    private JMenuItem fileRemoveAllFilesPlayList;
    private JMenuItem exit;
    private JMenu edit;
    private JRadioButtonMenuItem editSetRandomPlayMode;
    private JMenu help;
    private JMenuItem helpAbout;
    
    //TO-DO: ActionListener Methoden Methoden aufrufen lassen -> Übersicht
    public Menu()
    {
        super();
        //File Menu
        file = new JMenu("File");
        fileAddFilePlayList = new JMenuItem("Add File to Playlist...");
        fileAddFilePlayList.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent event)
        	{
        		Object answer = JOptionPane.showInputDialog(null, "Input path:", "Add File",
                        JOptionPane.INFORMATION_MESSAGE); 
        		if(answer instanceof String)
        		{
        			try
        			{
        				Global.fileCrawler.addFile(new File((String)answer));
        			}
        			catch(Exception e)
        			{
        			}
        		}
        	}
        });
        file.add(fileAddFilePlayList);
        fileAddFolderPlayList = new JMenuItem("Add Folder to Playlist...");
        fileAddFolderPlayList.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent event)
        	{
        		Object answer = JOptionPane.showInputDialog(null, "Input path:", "Add Folder",
                        JOptionPane.INFORMATION_MESSAGE); 
        		if(answer instanceof String)
        		{
        			try
        			{
        				Global.fileCrawler.addDirectory(new File((String)answer));
        			}
        			catch(Exception e)
        			{
        			}
        		}
        	}
        });
        file.add(fileAddFolderPlayList);
        fileRemoveAllFilesPlayList = new JMenuItem("Remove all Files from Playlist");
        fileRemoveAllFilesPlayList.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent event)
        	{
        		Global.playList.removeAllElements();
        	}
        });
        file.add(fileRemoveAllFilesPlayList);
        file.add(new JSeparator());
        exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent event)
        	{
        		System.exit(0);
        	}
        });
        file.add(exit);
        add(file);
        
        //Edit Menu
        edit = new JMenu("Edit");       
        editSetRandomPlayMode = new JRadioButtonMenuItem("Random Playback");
        editSetRandomPlayMode.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent event)
        	{
        		if(Global.player.getPlayMode()==0)
        		{
        			Global.player.setPlayMode(1);
        		}
        		else
        		{
        			Global.player.setPlayMode(0);
        		}
        	}
        });
        edit.add(editSetRandomPlayMode);        
        add(edit);
        
        //Help Menu
        help = new JMenu("Help");
        helpAbout = new JMenuItem("About");
        helpAbout.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		JOptionPane.showMessageDialog(null, 
        				"Author: Martin Braun \n" +
        				"Website: www.notcreatedyet.com \n" +
        				"Released under GPLv3 License \n" +
        				"Some Parts are under different Licences \n" +
        				"See readme.txt for further information");
        	}
        });
        help.add(helpAbout);
        add(help);
    }
    
//    public void updatePlayModes()
//	{
//		switch(Global.player.getPlayMode())
//		{
//		case 0:
//			editSetNormalPlayMode.setText("[x] Normal Playback");
//			editSetRandosmPlayMode.setText("[]  Random Playback");
//			break;
//		case 1:
//			editSetNormalPlayMode.setText("[]  Normal Playback");
//			editSetRandosmPlayMode.setText("[x] Random Playback");
//			break;
//		}
//	}
}
