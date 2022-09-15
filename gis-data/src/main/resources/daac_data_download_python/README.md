# How to Access the LP DAAC Data Pool with Python
---
# Objective:
The [DAACDataDownload.py](https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/DAACDataDownload.py) script demonstrates how to configure a connection to download data directly in Python from an Earthdata Login-enabled server, specifically the [LP DAAC Data Pool](https://e4ftl01.cr.usgs.gov/). The script is a command line executable, where a user will submit either a single URL to a file to be downloaded, or the location of a text file containing multiple URLs to files on the LP DAAC Data Pool to be downloaded, and a desired directory to download files to. The script uses a [netrc file](https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/.netrc) to safely store your [NASA Earthdata Login](https://urs.earthdata.nasa.gov) credentials that are needed to authenticate when downloading data from the LP DAAC Data Pool.

The script begins by determining if your OS already has a netrc file located in your home directory, and if so, if it is configured properly for `urs.earthdata.nasa.gov`. If it is determined that a netrc file does not exist, the script will prompt you for your NASA Earthdata Login Username and Password.  After submitting your credentials, the script will create a netrc file on your OS. (Note: you will only be prompted for your credentials the first time, if you already have a netrc properly configured in your home directory, these steps will be skipped). From there, the script uses the URL(s) that you provided in order to authenticate your NASA Earthdata Login credentials, submit a request for data using the `requests` package and ultimately download the desired data. If multiple URLs are included in the file list, the script will loop through and perform the aforementioned steps for each file. The output file name will be the same as the input file name.  

## NASA Earthdata Login:
**You will need a NASA Earthdata Login account in order to download LP DAAC data (and consequently use this script).** To create a NASA Earthdata Login account, go to the [Earthdata Login website](https://urs.earthdata.nasa.gov) and click the “Register” button, which is next to the green “Log In” button under the Password entry box. Fill in the required boxes (indicated with a red asterisk), then click on the “Register for Earthdata Login” green button at the bottom of the page. An email with instructions for activating the registration completes the process.

To download data from the LP DAAC archive, you need to authorize our applications to view your NASA Earthdata Login profile. Once authorization is complete, you may resume your session.
To authorize Data Pool, please [click here](https://urs.earthdata.nasa.gov/approve_app?client_id=ijpRZvb9qeKCK5ctsn75Tg&_ga=2.128429068.1284688367.1541426539-1515316899.1516123516).  

---
# Prerequisites:
*Disclaimer: Script has been tested on Windows and MacOS using the specifications identified below.*  
+ #### Python version 3.6  
  + `Requests`  
  + For a complete list of required packages, check out [DDD_WindowsOS.yml](https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/DDD_WindowsOS.yml) (Windows users) or [DDD_MacOS.yml](https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/DDD_MacOS.yml) (MacOS users).  
---
# 基本步骤:
## Getting Started:
> #### 1. 从 [LP DAAC Data Pool](https://e4ftl01.cr.usgs.gov/) 获取目标文件的 URL ，或者从  [NASA Earthdata Search Client](https://search.earthdata.nasa.gov/) 下载一个链接文件（包含所有目标文件的链接）.     
> #### 2.	下载 [DAACDataDownload.py](https://git.earthdata.nasa.gov/projects/LPDUR/repos/ecostress_swath2grid/browse/ECOSTRESS_swath2grid.py)    
  > 1. 从 https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/DAACDataDownload.py 获取, clicking inside of the file, pressing `control + a` followed by `control + c` and then opening a new file in R and pressing `control + v`.   
  > 2. You can download the script by going to: https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/raw/DAACDataDownload.py?at=refs%2Fheads%2Fmaster. Right-click and select `save as`, under the 'Save as type' dropdown, switch to `All Files` and change the file extension to `.R`, and then click save.  
  > 3. Additionally you can download all contents of this repository as a [zip file](https://git.earthdata.nasa.gov/rest/api/latest/projects/LPDUR/repos/daac_data_download_python/archive?format=zip).   
## Python Environment Setup
> #### 1. 推荐使用 [Conda](https://conda.io/docs/) 来配置python环境. https://www.anaconda.com/download/. 
> #### 2. Windows Setup
> 1.  下载 [DDD_WindowsOS.yml](https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/DDD_WindowsOS.yml) .
> 2. 打开 `DDD_WindowsOS.yml` 文件，修改前缀以匹配系统上的 Anaconda 安装路径, 保存文件. 
  > 2a. 如: `C:\Username\Anaconda3\envs\ecostress` -- 替换'Username' 为你的 Conda 安装目录 (保留 `Anaconda3\envs\ecostress`)  
  > 2b. 提示: 使用以下命令搜索安装路径  `dir Anaconda3 /AD /s`
> 3. 在命令行中定位到解压后的目录, 输入命令创建虚拟环境 `conda env create -f DDD_WindowsOS.yml`
> 4. 定位到 `DAACDataDownload.py` 所在目录
> 5. 激活前面创建的虚拟环境 
  > 1. 输入`activate DDD`  
> #### 3. MacOS Setup
> 1.  Download the [DDD_MacOS.yml](https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/DDD_MacOS.yml) file from the repository.
> 2. Open the `DDD_MacOS.yml` file with your favorite text editor, change the prefix to match the location of Anaconda on your OS, and save the file.  
  > 2a. Ex: `/anaconda3/envs/ecostress` if you downloaded conda under a local user directory, add `/Users/<insert username>` before `anaconda3` (leave `Anaconda3/envs/ecostress`)  
  > 2b. Tip: search for the location of Conda on your OS by opening the terminal and typing `find / -name directoryname -type d`
> 3. Navigate to the directory containing the `DDD_MacOS.yml` file in your Command Prompt, and type `conda env create -f DDD_MacOS.yml`
> 4. Navigate to the directory where you downloaded the `DAACDataDownload.py` script
> 5. Activate Python environment (created in step 3) in the Command Prompt   
    > 5a. Type `source activate DDD`  

[Additional information](https://conda.io/docs/user-guide/tasks/manage-environments.html) on setting up and managing Conda environments.
## Script Execution
> #### 1.	创建Python虚拟环境并激活后,  在命令行中执行脚本:
  > 1.  `python DAACDataDownload.py -dir <保存下载文件的目录> -f <一个 granule URL, 或 包含granule URLs的文件的路径>`  
  > 2. 如:   `python C:\User\Downloads\DAACDataDownload.py  -dir C:\User\datapool_downloads -f C:\User\datapool_downloads\MYD13-Point-Test-3-granule-list.txt` (或 `-f https://e4ftl01.cr.usgs.gov//DP107/MOLA/MYD13Q1.006/2013.01.09/MYD13Q1.A2013009.h09v07.006.2015254175244.hdf`)
  > 3. 若你没有在主目录（home directory）下配置 `netrc` 文件，该脚本会弹出提示框让你输入你的 NASA Earthdata Login Username 和 Password. 输入相应信息以继续下载数据  
  > 4. 数据将会下载到上面的 `-dir` 所指定的目录
## 配置 netrc 文件:  
若想手动创建自己的 netrc, 下载 [.netrc file template](https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/.netrc) , 添加自己的密码（credentials）, 保存到你的主目录（ home directory）. 若想使用 python 脚本设置 netrc 文件但不想下载任何模板文件， 则可拷贝或下载 [EarthdataLoginSetup.py](https://git.earthdata.nasa.gov/projects/LPDUR/repos/daac_data_download_python/browse/EarthdataLoginSetup.py) 脚本并执行: `python EarthdataLoginSetup.py`. 会提示输入NASA Earthdata Login Username 和 Password, 输入后回车以保存验证信息.

---
# Contact Information:
#### Author: Cole Krehbiel¹   
**Contact:** LPDAAC@usgs.gov  
**Voice:** +1-866-573-3222  
**Organization:** Land Processes Distributed Active Archive Center (LP DAAC)  
**Website:** https://lpdaac.usgs.gov/  
**Date last modified:** 11-20-2018  

¹Innovate!, Inc., contractor to the U.S. Geological Survey, Earth Resources Observation and Science (EROS) Center,  
 Sioux Falls, South Dakota, USA. Work performed under USGS contract G15PD00467 for LP DAAC².  
²LP DAAC Work performed under NASA contract NNG14HH33I.