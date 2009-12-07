package com.cve.fs;

import com.jcraft.jsch.UserInfo;

final class UserInfoValue implements UserInfo {
	
    final String passphrase;
    final String password;

    UserInfoValue(String passphrase, String password) {
            this.passphrase = passphrase;
            this.password = password;
    }

    @Override
    public String getPassphrase() {
            return passphrase;
    }

    @Override
    public String getPassword() {
            return password;
    }

    @Override
    public boolean promptPassphrase(String message) {
    showMessage(message);
            return false;
    }

    @Override
    public boolean promptPassword(String message) {
    showMessage(message);
            return true;
    }

    @Override
    /**
    "The authenticity of host '" + host + "' can't be established.\n" +
    "key_type + " key fingerprint is " + key_fprint + ".\n" +
    "Are you sure you want to continue connecting?");
     */
    public boolean promptYesNo(String message) {
        showMessage(message);
		return true;
	}

	@Override
	public void showMessage(String message) {
        System.out.println(message);
    }
}