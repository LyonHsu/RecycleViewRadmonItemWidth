# RecycleView 隨機 Item 寬度 擺放

17 video Code Task 
https://drive.google.com/file/d/1Bj5xyAzQFdhMnSVlIfxyS0-3hCH-nDQ2/view

Goal : Implement an Android application including search and display users from GitHub API. 
Features: 
● An Edittext for user to input keyword. 
● Use RecyclerView to present search results. 
● Each result item should include user’s avatar and name, 
● And every item will show one of three styles by random 
○ 1st : 1x1 
○ 2nd : 2x1 
○ 3rd : 2x2 
● Pagination (automatically loading next page) 
What we need: 
● 100% kotlin codebase. 
● Clean and friendly user interface. 
● Use of an architectural pattern such as MVVM and other. 
● Feel free to use third-party libraries to help with networking layer setup or asynchronous image download tasks for saving initial time, but please make sure you understand the concept. 
● Keep your source code clean, organized, and modularized, we will be reviewing them. 
● Please make sure you have handled all error states and displayed corresponding messages. 
Nice to have: 
● Use of Architecture Components. 
● Use of industry standard libraries such as Koin, Dagger2, RxJava. 
References: 
● https://developer.github.com/v3/guides/traversing-with-pagination/#constructin g-pagination-links 
● https://developer.github.com/v3/search/#search-users 
Submission: 
● Submit your code to any Git platform, once you've submitted the code, please don't update it afterwards. 
● Once you finish the task, please email our recruitment team with the link.


#API
  使用Vollet 獲取https://api.github.com/search/users?q=keyWord&page=%p 使用者資料
  其中
    keyWord  代表 要搜尋的使用者
    %p       代表 頁面
    
  回傳
   
     {
    "total_count": 1,
    "incomplete_results": false,
    "items": [
        {
          "login": "LyonHsu",
          "id": 23069910,
          "node_id": "MDQ6VXNlcjIzMDY5OTEw",
          "avatar_url": "https://avatars1.githubusercontent.com/u/23069910?v=4",
          "gravatar_id": "",
          "url": "https://api.github.com/users/LyonHsu",
          "html_url": "https://github.com/LyonHsu",
          "followers_url": "https://api.github.com/users/LyonHsu/followers",
          "following_url": "https://api.github.com/users/LyonHsu/following{/other_user}",
          "gists_url": "https://api.github.com/users/LyonHsu/gists{/gist_id}",
          "starred_url": "https://api.github.com/users/LyonHsu/starred{/owner}{/repo}",
          "subscriptions_url": "https://api.github.com/users/LyonHsu/subscriptions",
          "organizations_url": "https://api.github.com/users/LyonHsu/orgs",
          "repos_url": "https://api.github.com/users/LyonHsu/repos",
          "events_url": "https://api.github.com/users/LyonHsu/events{/privacy}",
          "received_events_url": "https://api.github.com/users/LyonHsu/received_events",
          "type": "User",
          "site_admin": false,
          "score": 1.0
        }
      ]
    }
    
 其中 total_count 是 資料的數量
 每次最多30筆 
 
 #注意：
 
     Rate limit
    当使用Basic Authentication, OAuth, 或者 client ID and secret请求的时候，每分钟最多可以请求30次(30 requests per minute)，如果没有认证的请求，则每分钟最多10次请求(10 requests per minute)。
    ————————————————
    版权声明：本文为CSDN博主「杨小熊的笔记」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/Next_Second/article/details/78238328

  
  
 
