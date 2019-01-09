package fz.cache.core;
/*

                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG

*/

import fz.cache.core.weiget.Encryption;

/**
 * 项目名称: z
 * 类描述： 加解密处理
 * 创建时间:2019/1/7 11:50
 *
 * @author tanping
 */
public class DefaultEncryption implements Encryption {
    @Override
    public boolean init() {
        return false;
    }

    @Override
    public String encrypt( String value) throws Exception {
        return value;
    }

    @Override
    public String decrypt(String value) throws Exception {
        return value;
    }
}
