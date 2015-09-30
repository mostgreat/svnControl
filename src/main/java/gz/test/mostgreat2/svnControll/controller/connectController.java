package gz.test.mostgreat2.svnControll.controller;

import gz.test.mostgreat2.common.model.SimpleResult;
import gz.test.mostgreat2.svnControll.model.DeployInfo;
import gz.test.mostgreat2.svnControll.model.DeployInfoWrapper;
import gz.test.mostgreat2.svnControll.model.SvnInfo;
import gz.test.mostgreat2.svnControll.model.SvnInfoWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc2.ng.SvnDiffGenerator;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.DefaultSVNRepositoryPool;
import org.tmatesoft.svn.core.wc.ISVNRepositoryPool;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnDiff;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping("svn")
public class connectController {

	private static final Logger logger = LoggerFactory.getLogger(connectController.class);
	private static String SVN_URL = "";
	private static String SVN_USER = "";
	private static String SVN_PASSWORD = "";
	private static HashMap<String, String> SVN_COPY_DIR = null;
	
	public static String getContent(String path, String name) throws UnsupportedEncodingException{
		
		DAVRepositoryFactory.setup( );
		ByteArrayOutputStream baos = null;
        String filePath = "/" + path + name;
        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create( SVNURL.parseURIEncoded( SVN_URL ) );
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( SVN_USER , SVN_PASSWORD );
            repository.setAuthenticationManager( authManager );

            SVNNodeKind nodeKind = repository.checkPath( filePath , -1 );
            
            if ( nodeKind == SVNNodeKind.NONE ) {
                logger.debug( "There is no entry at '" + SVN_URL + "'." );
            } else if ( nodeKind == SVNNodeKind.DIR ) {
            	logger.debug( "The entry at '" + SVN_URL + "' is a directory while a file was expected." );
            }
            Map fileProperties = new HashMap( );
            SVNProperties svnProperties = new SVNProperties();
            baos = new ByteArrayOutputStream( );
            repository.getFile( filePath , -1 , svnProperties , baos );
            
            //String mimeType = ( String ) svnProperties.get( SVNProperty.MIME_TYPE );
            String mimeType = svnProperties.getStringValue( SVNProperty.MIME_TYPE );
            boolean isTextType = SVNProperty.isTextMimeType( mimeType );

            Iterator iterator = fileProperties.keySet( ).iterator( );
            /*while ( iterator.hasNext( ) ) {
                String propertyName = ( String ) iterator.next( );
                String propertyValue = ( String ) fileProperties.get( propertyName );
                System.out.println( "File property: " + propertyName + "=" + propertyValue );
            }*/

            if ( !isTextType ) {
            	return "Not a text file.";
            	
            }
        }catch(Exception e){
        	logger.debug(e.getMessage());
        }
        return baos.toString("UTF-8");
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public @ResponseBody SvnInfoWrapper loginPage(
			@RequestParam(value="svnUrl", required= true) String svnUrl,
			@RequestParam(value="svnUser", required= true) String svnUser,
			@RequestParam(value="svnPassword", required= true) String svnPassword,
			HttpServletRequest req) throws Exception {
		
		logger.debug("SVN Connect TRY");
		
		SVNRepositoryFactoryImpl.setup();
		SVN_URL = svnUrl + "/";
		SVN_USER = svnUser;
		SVN_PASSWORD = svnPassword;
		
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(svnUser, svnPassword);
		repository.setAuthenticationManager(authManager);
		repository.testConnection();
		
		logger.debug("SVN URL IS = " + repository.getLocation().toString());
		SvnInfoWrapper result = new SvnInfoWrapper();
		result.setSvnList(listEntries(repository, ""));
		List<String> rootDir = getChildList(repository ,result.getSvnList());
		result.setRootList(rootDir);
		return result;
	}
	
	@RequestMapping(value = "/getContent.do", method = RequestMethod.POST)
	public @ResponseBody String getContent( HttpServletRequest req
										    ,@RequestParam("name") String name
										    ,@RequestParam("path") String path,
										    Model model) throws Exception {
		
		logger.debug("SVN Get Content");
		logger.debug(path + name);
		
		return getContent(path, name);
		
	}
	
	@RequestMapping(value = "/getDiff.do")
	public @ResponseBody String seeDiff( HttpServletRequest req
										    ,@RequestParam("sourceUrl") String sourceUrl
										    ,@RequestParam("destinationUrl") String destinationUrl
										    ,@RequestParam("sourceRevision") String sourceRevision
										    ,@RequestParam("destinationRevision") String destinationRevision
										    ) throws Exception {
		String result = "";
		SVNRepository repository = null;
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
	    try {
	    	
	    	repository = SVNRepositoryFactory.create( SVNURL.parseURIEncoded( SVN_URL ) );
	        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( SVN_USER , SVN_PASSWORD );
	        repository.setAuthenticationManager( authManager );
	        
	        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        final SvnDiffGenerator diffGenerator = new SvnDiffGenerator();
	        diffGenerator.setBasePath(new File(""));
	        
	        final SVNURL url1 = SVNURL.parseURIEncoded( SVN_URL + sourceUrl );
	        final SVNURL url2 = SVNURL.parseURIEncoded( SVN_URL + destinationUrl);
	        logger.debug("Source SVN URL is = " + url1);
	        logger.debug("Target SVN URL is = " + url2);
	        
	        final SVNRevision svnRevision1 = SVNRevision.parse(sourceRevision);
	        final SVNRevision svnRevision2 = SVNRevision.parse(destinationRevision);
	        
	        logger.debug("Source SVN URL Revision = " + svnRevision1);
	        logger.debug("Target SVN URL Revision = " + svnRevision2);
	        
	        final SvnDiff diff = svnOperationFactory.createDiff();
	        svnOperationFactory.setAuthenticationManager(authManager);
	        diff.setSources(SvnTarget.fromURL(url1 , svnRevision1 ), SvnTarget.fromURL(url2, svnRevision2));
	        diff.setDiffGenerator(diffGenerator);
	        diff.setOutput(byteArrayOutputStream);
	        diff.run();
	        logger.debug("Diff Result = " + byteArrayOutputStream);
	        result = new String(byteArrayOutputStream.toByteArray()).replace(System.getProperty("line.separator"), "\n");
	        result = ( result.trim().length() == 0 ) ? "No difference" : result;
	        
	    } finally {
	        svnOperationFactory.dispose();
	    }
		
		return result;
		
	}
	
	@RequestMapping(value = "/deploy.do",  method = RequestMethod.POST,  consumes = "application/json")
	public @ResponseBody SimpleResult deployCheckedList( HttpServletRequest req
											,@RequestBody DeployInfoWrapper wrapper
										    ) throws Exception {
		
		SimpleResult result = new SimpleResult();
		SVN_COPY_DIR = new HashMap<String, String>();
		ISVNEditor commitEditor = null;
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final ISVNRepositoryPool repositoryPool = new DefaultSVNRepositoryPool(null, null);
		SVNRepository repository2 = null;
		
		String checksum = "";
		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
	    try {
			//repository = SVNRepositoryFactory.create( SVNURL.parseURIEncoded( SVN_URL ) );
			final SVNRepository repository = repositoryPool.createRepository(SVNURL.parseURIEncoded( SVN_URL ), true);
			repository2 = repositoryPool.createRepository(SVNURL.parseURIEncoded( SVN_URL ), false);
			
	        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( SVN_USER , SVN_PASSWORD );
	        repository.setAuthenticationManager( authManager );
	        repository2.setAuthenticationManager( authManager );
	        SVNClientManager manager = SVNClientManager.newInstance();
	        manager.setAuthenticationManager(authManager);
	        String root = SVN_URL + wrapper.getDeployDir() + "/";
	        
	        //한번 초기화
	        commitEditor = repository.getCommitEditor("commit automatically", null, true ,null);
	        commitEditor.openRoot(-1);
	        for(DeployInfo temp : wrapper.getDeploys()){
	        	if(temp.isFile()){
	        	String[] splitWord = temp.getFilePath().split("/");
	        	String addedDir = "";
	        	for(int i = 1  ; i < splitWord.length ; i++){
	        		addedDir += "/" + splitWord[i]; 
	        		if(chkDirExist(repository2, wrapper.getDeployDir() + addedDir.replace("//", "/"))){
	        			commitEditor.addDir(wrapper.getDeployDir() + addedDir, null, -1);
	        		}
	        	}
	        	logger.debug(wrapper.getDeployDir() + "/" +  addedDir + "/" + temp.getFileName() + "," + temp.getRevision() );
	        		if(chkFileExist(repository2, wrapper.getDeployDir() + addedDir + "/" + temp.getFileName() )){ //there is no file already exists
		        		commitEditor.openDir(wrapper.getDeployDir() +  addedDir , 1);
						commitEditor.addFile(wrapper.getDeployDir() +  addedDir + "/" + temp.getFileName(), null, -1);
			            commitEditor.changeFileProperty(wrapper.getDeployDir() + addedDir + "/" + temp.getFileName() , "filePropertyName", SVNPropertyValue.create("filePropertyValue"));
			            commitEditor.applyTextDelta( wrapper.getDeployDir() + addedDir + "/" + temp.getFileName(), null);
		
			            final ByteArrayInputStream fileContentsStream = new ByteArrayInputStream( getContent(temp.getFilePath(), temp.getFileName()).getBytes());
			            checksum = deltaGenerator.sendDelta(wrapper.getDeployDir() + addedDir + "/" + temp.getFileName(), fileContentsStream, commitEditor, true);
			            fileContentsStream.close();
			            commitEditor.closeFile(wrapper.getDeployDir() + addedDir + "/" + temp.getFileName(), checksum);
			            commitEditor.closeDir();
			            
	        		}else{
		        		commitEditor.openDir(wrapper.getDeployDir() + addedDir , 1);
		        		commitEditor.openFile(wrapper.getDeployDir() + addedDir + "/" + temp.getFileName(), -1);
			            commitEditor.changeFileProperty(wrapper.getDeployDir() + addedDir + "/" + temp.getFileName() , "filePropertyName", SVNPropertyValue.create("filePropertyValue"));
			            commitEditor.applyTextDelta( wrapper.getDeployDir() + addedDir + "/" + temp.getFileName(), null);
		
			            final ByteArrayInputStream fileContentsStream = new ByteArrayInputStream( getContent(temp.getFilePath(), temp.getFileName()).getBytes());
			            checksum = deltaGenerator.sendDelta(wrapper.getDeployDir() + addedDir + "/" + temp.getFileName(), fileContentsStream, commitEditor, true);
			            fileContentsStream.close();
			            commitEditor.closeFile(wrapper.getDeployDir() + addedDir + "/" + temp.getFileName(), checksum);
			            commitEditor.closeDir();
			            
	        		}
	        	}
			}
	        
	        SVNCommitInfo info = commitEditor.closeEdit();
    		long newRevision = info.getNewRevision();
    		logger.debug("=======================");
    		logger.debug("=======================");
    		logger.debug("new Revision = " + newRevision);
    		logger.debug("=======================");
    		logger.debug("=======================");
			result.setResult("success");	    	
	    } catch(Exception e) {
	    	result.setResult("fail");
	    	logger.debug(e.getMessage());
	    } finally {
	    	repositoryPool.dispose();
	        svnOperationFactory.dispose();
	        if (repository2 != null) {
	            //it should be closed by hand because it was created with mayReuse=false
	            repository2.closeSession();
	        }
	    }
		
		return result;
		
	}
	
	public static List<String> getChildList(SVNRepository repository, List<SvnInfo> svnInfo) throws SVNException {
		
		List<String> rootDir = new ArrayList<String>(); 
		
		for(SvnInfo temp: svnInfo){
			if(temp.isOpen()){ //sub directory exist
				//logger.debug("===============" + temp.getName() + "===" + temp.getPath() );
				if(!temp.getPath().endsWith("/"))
					rootDir.add(temp.getName());
				temp.setSubTree(listEntries(repository, 
						temp.getPath().endsWith("/") ? temp.getPath() + temp.getName() : temp.getName() ));
				getChildList(repository, temp.getSubTree());
			}
		}
		
		return rootDir;
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
            svnInfo.setOpenStatus(false);
            svnInfo.setChk(false);
            svnInfo.setFile(false);
            
            if (entry.getKind() == SVNNodeKind.DIR) {
            	svnInfo.setOpen(true);  //If this has child or not
            	svnInfo.setOpenStatus(false);
            	svnInfo.setFile(false);
            }else if(entry.getKind() == SVNNodeKind.FILE){
            	svnInfo.setFile(true);            	
            }
            svnInfolist.add(svnInfo);
        }
        
        return svnInfolist;
    }
	
	public static boolean chkDirExist(SVNRepository repository, String path) throws SVNException {
        
		logger.debug("================================");
		logger.debug("check Dir exist");
		logger.debug("path : " + path);
		boolean result = false;
		SVNNodeKind nodeKind = repository.checkPath(path, -1);
		if(nodeKind != SVNNodeKind.DIR) {
	          	result = true;            	            	
	    }
		if( SVN_COPY_DIR.get(path.trim()) != null){
			result = false;
		}
		SVN_COPY_DIR.put(path, "visit");
		logger.debug("kind : " + nodeKind);
		logger.debug("result : " + result);
		logger.debug("================================");
		return result;
    }
	
	public static boolean chkFileExist(SVNRepository repository, String path) throws SVNException {
		
		logger.debug("================================");
		logger.debug("check file exist");
		logger.debug("path : " + path);
		boolean result = false;
		SVNNodeKind nodeKind = repository.checkPath(path, -1);
		if(nodeKind != SVNNodeKind.FILE) {
	          	result = true;            	            	
	       }
		logger.debug("kind : " + nodeKind);
		logger.debug("result : " + result);
		logger.debug("================================");
		return result;
    }
}
