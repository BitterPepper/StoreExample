package com.developer.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.developer.rest.WarehouseService;

@ApplicationPath("/*")
public class RSApplication extends Application
{
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(WarehouseService.class);
        return s;
    }
}