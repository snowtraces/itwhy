package com.snowtraces.itwhy.service.impl;

import com.snowtraces.itwhy.entity.Com;
import com.snowtraces.itwhy.repository.ComMapper;
import com.snowtraces.itwhy.service.ComService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Service
public class ComServiceImpl extends ServiceImpl<ComMapper, Com> implements ComService {

}
