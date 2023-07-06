# AutoBackupPlugin
这是一个为MC Bukkit服务器自动备份地图的插件。
A plugin for automatically saving maps for MC Bukkit servers.

****

## 使用前必读   Very important
### 免责声明：
该插件仅作为服务器地图备份的辅助手段，并不保证其数据完整性。若由于使用该插件而造成了任何损失，**该插件作者概不负责**。使用该插件即视为您已知晓并同意上述条款。
### Disclaimers:
This plug-in is only an auxiliary means for server map backup, and does not guarantee its Data integrity. If any losses are caused due to the use of this plugin, **the plugin author is not responsible**. By using this plugin, you are deemed to be aware of and agree to the above terms.

****

## 用法   Usage
将发行的jar文件置于MC Bukkit服务器plugins文件夹下即可。  
Place the released jar files in the plugins folder of the MC Bukkit server.

配置文件在plugins/AutoBackupPlugin/conf.json  
The configuration file is located in plugins/AutoBackupPlugin/conf.json

### conf.json
#### `maxWorldNum`
设置备份最多储存世界文件个数  
Set the maximum number of world files stored for backup
#### `saveIntervalTicks`
设置每经过多少游戏tick备份一次  
Set after how many game ticks backed up once
#### `enableZip`
启用zip压缩，即保存为zip文件  
Enable zip compression(save as a zip file)
#### `saveFolderPath`
备份文件保存路径  
Backup file save path

### 指令用法(均需管理员权限)
### Instruction Usage(All require op)
#### `/backup saveNow`
即刻保存  
Save Now
#### `/backup reloadConfig`
配置热重载  
Configure thermal overload
#### `/backup set maxWorldNum <int>`
设置maxWorldNum并更新conf.json  
Set maxWorldNum and update conf.json
#### `/backup set saveIntervalTicks <int>`
设置saveIntervalTicks并更新conf.json  
Set saveIntervalTicks and update conf.json
#### `/backup set enableZip <boolean>`
设置enableZip并更新conf.json  
Set enableZip and update conf.json
     
