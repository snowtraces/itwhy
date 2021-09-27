package com.snowtraces.itwhy.service.impl;

import com.snowtraces.itwhy.entity.Cat;
import com.snowtraces.itwhy.repository.CatMapper;
import com.snowtraces.itwhy.service.CatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类 服务实现类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Service
public class CatServiceImpl extends ServiceImpl<CatMapper, Cat> implements CatService {

}
