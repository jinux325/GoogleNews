package com.u.googlenews

import com.u.googlenews.main.MainViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp(){
        viewModel = MainViewModel()
    }

    @Test
    fun sortList(){
        val sortList = viewModel.sortList("복지부, 중산층 지원 제도에 ‘저소득층 선별’ 재산기준 고집전문가 \"소득인정액·재산기준 적용하면 5월 지급 힘들 수도\"소득 하위 70%에 최대")
        val result : List<Any> = listOf<Any>("재산기준","5월", "70%에", "고집전문가", "복지부", "선별", "소득", "소득인정액", "수도",  "저소득층", "적용하면", "제도에", "중산층", "지급", "지원", "최대", "하위", "힘들")

        val sortList1 = viewModel.sortList("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세")
        val result1 : List<Any> = listOf<Any>("닳도록", "동해물과", "마르고", "만세", "백두산이", "보우하사", "우리나라", "하느님이")

        // 2글자 이상 필터
        // return : []
        val sortList2 = viewModel.sortList("1 1 2 22 22 3 3 4 4 4 4 4 4 55 5 5 6")
        val result2 : List<Any> = listOf<Any>("22", "55")

        val sortList3 = viewModel.sortList("무궁화 삼천리 화려 강산 대한 사람 대한으로 길이 보전하세")
        val result3 : List<Any> = listOf<Any>("강산", "길이", "대한", "대한으로", "무궁화", "보전하세", "사람", "삼천리", "화려")

        val sortList4 = viewModel.sortList("11 11 22 22 22 33 33 44 44 44 44 44 44 55 55 55 66")
        val result4 : List<Any> = listOf<Any>("44", "22", "55", "11", "33", "66")

        Assert.assertEquals(result, sortList)
        Assert.assertEquals(result1, sortList1)
        Assert.assertEquals(result2, sortList2)
        Assert.assertEquals(result3, sortList3)
        Assert.assertEquals(result4, sortList4)
    }
}