/**
 * 本包用于存放方法请求参数，方法请求参数也可以是自定义查询的参数。按业务模块划分子包。<br>
 * param中bean的命名最好有一定规律，易于区分。<br>
 * 目前采用的命名方式是：动词+对象。动词包括：<br>
 * 1.新增——add(新增对象或者给对象新增某属性或者追加属性)<br>
 * 2.修改——set(修改对象某属性，且此属性本可能无值)；update(修改对象某属性，且此属性本可能有值)<br>
 * 3.删除——delete(删除对象)；remove(删除对象某属性)<br>
 * 4.查询——query(查询对象)<br>
 * 另外，如果指明为批量操作，则在动词前加batch。如果指明为单个操作（指一种不允许发生多个操作的操作），则在动词前加single。<br>
 * 对于多对象联合操作，按业务含义将对象名称从前至后排列。<br>
 * 对于嵌套对象，不作额外处理。<br>
 * @author Frodez
 * @date 2019-03-11
 */
package frodez.dao.param;
