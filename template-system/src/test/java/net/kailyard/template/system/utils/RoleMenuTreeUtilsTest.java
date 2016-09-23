package net.kailyard.template.system.utils;

import org.junit.Assert;
import org.junit.Test;

public class RoleMenuTreeUtilsTest {
    @Test
    public void test() throws Exception{
        Long id = RoleMenuTreeUtils.convertId("menu_1", RoleMenuTreeUtils.MENU_PRE);
        Assert.assertEquals(id.longValue(), 1l);
        id = RoleMenuTreeUtils.convertId("menu_12", RoleMenuTreeUtils.MENU_PRE);
        Assert.assertEquals(id.longValue(), 12l);
        id = RoleMenuTreeUtils.convertId("permission_2", RoleMenuTreeUtils.PERMISSION_PRE);
        Assert.assertEquals(id.longValue(), 2l);
        id = RoleMenuTreeUtils.convertId("permission_20", RoleMenuTreeUtils.PERMISSION_PRE);
        Assert.assertEquals(id.longValue(), 20l);
    }

}
