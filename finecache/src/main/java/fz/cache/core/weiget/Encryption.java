package fz.cache.core.weiget;

public interface Encryption {

  /**
   * Initialize the encryption algorithm, If the device does not support required
   * crypto return false
   *
   * @return true if crypto is supported
   */
  boolean init();

  /**
   * Encrypt the given string and returns cipher text
   *
   * @param value is the plain text
   *
   * @return cipher text as string
   */
  String encrypt( String value) throws Exception;

  /**
   * Decrypt the given cipher text and return plain text
   *
   * @param value is the cipher text
   *
   * @return plain text
   */
  String decrypt(String value) throws Exception;

}
