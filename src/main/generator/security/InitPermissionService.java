package security;

import frodez.BlogManagePlatformApplication;
import frodez.constant.enums.user.PermissionTypeEnum;
import frodez.constant.settings.PropertyKey;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.model.user.Permission;
import frodez.util.common.EmptyUtil;
import frodez.util.http.URLMatcher;
import frodez.util.json.JSONUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tk.mybatis.mapper.entity.Example;

/**
 * 根据所有controller初始化权限
 * @author Frodez
 * @date 2019-02-02
 */
public class InitPermissionService {

	public static void main(String[] args) {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		PermissionMapper permissionMapper = ContextUtil.get(PermissionMapper.class);
		List<Permission> permissionList = new ArrayList<>();
		Date date = new Date();
		BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextUtil.context(), HandlerMapping.class, true, false)
			.values().stream().filter((iter) -> {
				return iter instanceof RequestMappingHandlerMapping;
			}).map((iter) -> {
				return ((RequestMappingHandlerMapping) iter).getHandlerMethods().entrySet();
			}).flatMap(Collection::stream).forEach((entry) -> {
				String requestUrl = PropertyUtil.get(PropertyKey.Web.BASE_PATH) + entry.getKey().getPatternsCondition()
					.getPatterns().stream().findFirst().get();
				if (!URLMatcher.needVerify(requestUrl)) {
					return;
				}
				requestUrl = requestUrl.substring(PropertyUtil.get(PropertyKey.Web.BASE_PATH).length());
				String requestType = entry.getKey().getMethodsCondition().getMethods().stream().map(RequestMethod::name)
					.findFirst().orElse(PermissionTypeEnum.ALL.name());
				String permissionName = ReflectUtil.getShortMethodName(entry.getValue().getMethod());
				Permission permission = new Permission();
				permission.setCreateTime(date);
				permission.setUrl(requestUrl);
				permission.setName(permissionName);
				permission.setDescription(permissionName);
				if (requestType.equals("GET")) {
					permission.setType(PermissionTypeEnum.GET.getVal());
				} else if (requestType.equals("POST")) {
					permission.setType(PermissionTypeEnum.POST.getVal());
				} else if (requestType.equals("DELETE")) {
					permission.setType(PermissionTypeEnum.DELETE.getVal());
				} else if (requestType.equals("PUT")) {
					permission.setType(PermissionTypeEnum.PUT.getVal());
				} else {
					permission.setType(PermissionTypeEnum.ALL.getVal());
				}
				permissionList.add(permission);
			});
		System.out.println("权限条目数量:" + permissionList.size());
		System.out.println("权限详细信息:" + JSONUtil.string(permissionList));
		Example example = new Example(Permission.class);
		permissionMapper.deleteByExample(example);
		if (EmptyUtil.no(permissionList)) {
			permissionMapper.insertList(permissionList);
		}
		SpringApplication.exit(ContextUtil.context(), () -> 1);
	}

}
