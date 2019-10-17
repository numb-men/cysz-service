package cn.hengyumo.humor.base.mvc;


import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * BaseController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Slf4j
@Validated
public class BaseController<S extends BaseService<T, ID>, T extends BaseEntity<ID>, ID extends Serializable> {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected S baseService;


    @SystemUserAuth
    @GetMapping("/{id}")
    public Result findOne(@PathVariable ID id) {
        return Result.success(baseService.findOne(id));
    }

    @SystemUserAuth
    @GetMapping("/")
    public Result findOneById(@RequestParam ID id) {
        return Result.success(baseService.findOne(id));
    }

    @SystemUserAuth
    @GetMapping("/count")
    public Result count(){
        return Result.success(baseService.count());
    }

    @SystemUserAuth
    @GetMapping("/page")
    public Result findAllPaged(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return Result.success(baseService.findAll(pageNumber, pageSize));
    }

    @SystemUserAuth
    @GetMapping("/list")
    public Result findAll() {
        return Result.success(baseService.findAll());
    }

    @SystemUserAuth
    @PostMapping("/")
    public Result addOne(@Valid @RequestBody T entity) {
        return Result.success(baseService.save(entity));
    }

    @SystemUserAuth
    @PutMapping("/{id}")
    public Result updateOne(@Valid @RequestBody T entity, @PathVariable ID id) {
        entity.setId(id);
        return Result.success(baseService.save(entity));
    }

    @SystemUserAuth
    @PutMapping("/")
    public Result updateOneById(@Valid @RequestBody T entity, @RequestParam ID id) {
        entity.setId(id);
        return Result.success(baseService.save(entity));
    }

    @SystemUserAuth
    @DeleteMapping("/{id}")
    public Result deleteOne(@PathVariable ID id) {
        baseService.setDeletedTrue(id);
        return Result.success();
    }

    @SystemUserAuth
    @DeleteMapping("/")
    public Result deleteOneById(@RequestParam ID id) {
        baseService.setDeletedTrue(id);
        return Result.success();
    }
}
