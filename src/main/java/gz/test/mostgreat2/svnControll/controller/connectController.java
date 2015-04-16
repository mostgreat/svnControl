package gz.test.mostgreat2.svnControll.controller;

import gz.test.mostgreat2.svnControll.model.SvnInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping("svn")
public class connectController {

	private static final Logger logger = LoggerFactory.getLogger(connectController.class);
	private static List<SvnInfo> result = null;

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public @ResponseBody List<SvnInfo> loginPage(
			@RequestParam(value="svnUrl", required= true) String svnUrl,
			@RequestParam(value="svnUser", required= true) String svnUser,
			@RequestParam(value="svnPassword", required= true) String svnPasword,
			HttpServletRequest req) throws Exception {
		
		logger.debug("SVN Connect TRY");
		
		SVNRepositoryFactoryImpl.setup();
		//String svnUrl = "http://wua.social:7070/subversion";
		//String svnUser = "test";
		//String svnPasword = "1234";
		
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(svnUser, svnPasword);
		repository.setAuthenticationManager(authManager);
		repository.testConnection();
		
		logger.debug("SVN URL IS = " + repository.getLocation().toString());
		result = new ArrayList<SvnInfo>();
		result.addAll(listEntries(repository, ""));
		
		getChildList(repository ,result);
		
		return result;
		
	}
	
	@RequestMapping(value = "/getContents.do", method = RequestMethod.POST)
	public @ResponseBody String getContents( HttpServletRequest req
										    ,@RequestParam("inputData") SvnInfo svnInfo ) throws Exception {
		
		logger.debug("SVN Get Contents");
		logger.debug(svnInfo.toString());
		
		SVNRepositoryFactoryImpl.setup();
		String svnUrl = "http://wua.social:7070/subversion";
		String svnUser = "test";
		String svnPasword = "1234";
		
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(svnUser, svnPasword);
		repository.setAuthenticationManager(authManager);
		repository.testConnection();
		
		
		
		return "";
		
	}
	
	public static void getChildList(SVNRepository repository, List<SvnInfo> svnInfo) throws SVNException {
		
		
		for(SvnInfo temp: svnInfo){
			if(temp.isOpen()){ //sub directory exist
				System.out.println("===============" + temp.getName() + "===" + temp.getPath() );
				temp.setSubTree(listEntries(repository, 
						temp.getName().equals("trunk") 
					||  temp.getName().equals("tags")
					||  temp.getName().equals("branches")
						? temp.getName() : temp.getPath() + "/" + temp.getName() ));
				getChildList(repository, temp.getSubTree());
			}
		}
	}

	public static List<SvnInfo> listEntries(SVNRepository repository, String path) throws SVNException {
        
		Collection<SVNDirEntry> entries = repository.getDir(path, -1, null,(Collection) null);
        Iterator<SVNDirEntry> iterator = entries.iterator();
        List<SvnInfo> svnInfolist = new ArrayList<SvnInfo>();
        SvnInfo svnInfo = null; 

        while (iterator.hasNext()) {
            SVNDirEntry entry = iterator.next();
            svnInfo = new SvnInfo();
        
            /*System.out.println("/" + (path.equals("") ? "" : path + "/")
            					   + entry.getName() + " (author: '" + entry.getAuthor()
            					   + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");*/
            /*
             * Checking up if the entry is a directory.
             */
            svnInfo.setPath( path.equals("") ? entry.getName() : path + "/" );
            svnInfo.setName(entry.getName());
            svnInfo.setAuthor(entry.getAuthor());
            svnInfo.setRevision(entry.getRevision());
            svnInfo.setDate(entry.getDate().toString());
            svnInfo.setOpen(false);
            
            if (entry.getKind() == SVNNodeKind.DIR) {
            	svnInfo.setOpen(true);
            }
            svnInfolist.add(svnInfo);
        }
        
        return svnInfolist;
    }
}
