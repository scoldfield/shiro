package com.cmcc.basic.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.cmcc.basic.model.BackupFile;

public class BackupFileUtil {
    private static BackupFileUtil util;
    private String backupFile;
    private static String realPath;
    private String database;
    private String username;
    private String password;
    private List<String> backupFiles;
    private final static String DATABASE_NAME = "database";
    private final static String BACKUP_NAME = "backup";
    
    private BackupFileUtil() throws IOException{
        Properties prop = new Properties();
        prop.load(BackupFileUtil.class.getClassLoader().getResourceAsStream("backup.properties"));
        database = prop.getProperty("database");
        username = prop.getProperty("database_username");
        password = prop.getProperty("database_password");
        backupFile = prop.getProperty("backupFile");
        File bf = new File(realPath+File.separator+backupFile);
        if(!bf.exists()) bf.mkdirs();
        backupFiles = new ArrayList<String>();
        //���Ҫ���ݻ���Ҫ�ָ����ļ���
        String fs = prop.getProperty("file");
        String[] fas = fs.split(",");
        for(String f:fas) {
            backupFiles.add(f);
        }
        
    }
    
    public static BackupFileUtil getInstance(String realPath) {
        try {
            BackupFileUtil.realPath = realPath;
            if(util==null) util = new BackupFileUtil();
            return util;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * �б����Եı����ļ�
     * @return
     */
    public List<BackupFile> listBackups() {
        File [] fs = new File(realPath+File.separator+backupFile).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if(pathname.isDirectory())
                    return false;
                return true;
            }
        });
        List<BackupFile> bs = new ArrayList<BackupFile>();
        BackupFile bf = null;
        for(File f:fs) {
            bf = new BackupFile();
            bf.setName(f.getName());
            bf.setSize((int)(f.length()/1024));
            bf.setTime(new Date(f.lastModified()));
            bf.setFiletype(f.getName().substring(f.getName().lastIndexOf(".")+1));
            bs.add(bf);
        }
        Collections.sort(bs);
        return bs;
    }
    
    public void backup(String name) {
        String bp = realPath+File.separator+backupFile+File.separator+BACKUP_NAME;
        try {
            //1�����������ļ��ж���
            File bpf = new File(bp);
            bpf.mkdirs();
            //1���������ݿ�
            MySQLUtil msu = MySQLUtil.getInstance();
            msu.setCfg(DATABASE_NAME, bp, database, username, password);
            msu.backup();
            //2����Ҫ���ݵ��ļ��п�����Ŀ���ļ�����
            for(String f:backupFiles) {
                String src = realPath+File.separator+f;
                String dest = bp+f;
                FileUtils.copyDirectory(new File(src), new File(dest));
            }
            //3��tar��gz
            TarAndGzipUtil tagu = TarAndGzipUtil.getInstance();
            tagu.tarFile(bp,realPath+File.separator+backupFile+File.separator+new Date().getTime()+"_"+name+".tar");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                FileUtils.deleteDirectory(new File(bp));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    //�ָ���name���������ļ�������
    public void resume(String name) {
        String op = realPath+File.separator+backupFile+File.separator+BACKUP_NAME;
        try {
            //1�����ļ����н�ѹ��
            String fp = realPath+File.separator+backupFile+File.separator+name;
            TarAndGzipUtil tagu = TarAndGzipUtil.getInstance();
            tagu.unTarFile(new File(fp), realPath+File.separator+backupFile);
            //2���������Ҹ�����Ӧ���ļ���
            for(String f:backupFiles) {
                //��ɾ��ԭ�е��ļ���
                String src = op+f;
                String dest = realPath+File.separator+f;
                File dfd = new File(dest);
                if(!dfd.exists()) dfd.mkdirs();
                FileUtils.deleteDirectory(dfd);
                FileUtils.copyDirectory(new File(src), dfd);
            }
            //3���ָ����ݿ�
            MySQLUtil msu = MySQLUtil.getInstance();
            msu.setCfg(DATABASE_NAME, op, database, username, password);
            msu.resume();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                FileUtils.deleteDirectory(new File(op));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Ҫɾ�����ļ�������
     * @param name
     */
    public void delete(String name) {
        File f = new File(realPath+File.separator+backupFile+File.separator+name);
        f.delete();
    }
}