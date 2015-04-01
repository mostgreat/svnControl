package gz.test.mostgreat2.svnControll.controller;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public @ResponseBody String loginPage(HttpServletRequest req) throws Exception {
		
		String result = "";
		
		logger.debug("SVn Connect TRY");
		
		SVNRepositoryFactoryImpl.setup();
		String svnUrl = "http://wua.social:7070/subversion";
		String svnUser = "test";
		String svnPasword = "1234";
		
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(svnUser, svnPasword);
		repository.setAuthenticationManager(authManager);
		repository.testConnection();
		
		logger.debug("SVN URL IS = " + repository.getLocation().toString());
		listEntries(repository, "trunk");
		
		
		return result;
	}

	public static void listEntries(SVNRepository repository, String path) throws SVNException {
        
    	Collection<SVNDirEntry> entries = repository.getDir(path, -1, null,(Collection) null);
        Iterator<SVNDirEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = iterator.next();
            
            System.out.println("/" + (path.equals("") ? "" : path + "/")
            					   + entry.getName() + " (author: '" + entry.getAuthor()
            					   + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
            /*
             * Checking up if the entry is a directory.
             */
            if (entry.getKind() == SVNNodeKind.DIR) {
                listEntries(repository, (path.equals("")) ? entry.getName() : path + "/" + entry.getName());
            }
        }
    }
}
