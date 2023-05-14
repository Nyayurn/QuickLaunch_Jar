[![GitHub release](https://img.shields.io/github/release/ANMSakura/QuickLaunch_Jar)](https://github.com/ANMSakura/QuickLaunch_Jar/releases)
[![License](https://img.shields.io/github/license/alibaba/fastjson2?color=4D7A97&logo=apache)](https://www.apache.org/licenses/LICENSE-2.0.html)

# QuickLaunch_Jar
`QuickLaunch_Jar`是一个随心创作的小工具
  - 双击运行Jar包
  - 如果设置了多个java会弹窗选择
  - 支持平台: `Windows`
  - 未测试但可能支持平台: `Linux`, `MacOS`
  - 支持`Java8+`

# 使用前提:
  - 环境变量中设置有Java环境
  - 打开的.jar文件必须设置有入口(即java -jar xxx.jar必须能正常运行)

# 软件效果:  
![image](https://github.com/ANMSakura/QuickLaunch_Jar/assets/132195516/6c398861-921d-4e6a-a190-710ba1424cdc)  
![image](https://github.com/ANMSakura/QuickLaunch_Jar/assets/132195516/fbe261e7-a358-4021-9265-1611f47b990c)

# 使用方法:  
## 方法1(推荐):  
1. 右键任意.jar文件并点击属性  
    ![image](https://github.com/ANMSakura/QuickLaunch_Jar/assets/132195516/f5a92fe0-8bd9-40bd-ae56-8b591872e0a6)
2. 更改打开方式  
    ![image](https://github.com/ANMSakura/QuickLaunch_Jar/assets/132195516/a50cb459-276b-49bf-be41-c4eef6b69910)
3. 下滑点击“在电脑上选择应用”  
    ![image](https://github.com/ANMSakura/QuickLaunch_Jar/assets/132195516/c32150b6-ac36-434d-b398-cdbd09fd1e90)
4. 选择本软件
    ![image](https://github.com/ANMSakura/QuickLaunch_Jar/assets/132195516/337742b9-f4fc-40e6-937b-64705aa2787d)
5. 应用
6. 双击任意.jar文件即可运行
  
## 方法2: 将任意.jar文件拖拽到本软件的.exe文件上即可使用  
   ![image](https://github.com/ANMSakura/QuickLaunch_Jar/assets/132195516/6935e0bd-b16c-456d-bbe2-87d9c03279e6)

# 配置文件
  - 请放在与本软件同级目录下
  - 如果没有配置文件，则会自动使用环境变量的java运行jar(不弹出选择框)
  - 配置方法请参考JavaFiles.json
