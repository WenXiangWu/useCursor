import os
import requests
import ssl
from urllib3.poolmanager import PoolManager
from urllib3.util.ssl_ import create_urllib3_context

# 创建自定义的SSL上下文
class CustomHttpAdapter(requests.adapters.HTTPAdapter):
    def init_poolmanager(self, connections, maxsize, block=False):
        ctx = create_urllib3_context()
        ctx.set_ciphers('DEFAULT@SECLEVEL=1')
        self.poolmanager = PoolManager(
            num_pools=connections,
            maxsize=maxsize,
            block=block,
            ssl_version=ssl.PROTOCOL_TLSv1_2,
            ssl_context=ctx)

def main():
    # 创建目录
    output_dir = "src/main/resources/static/images/cards"
    os.makedirs(output_dir, exist_ok=True)
    print(f"创建目录: {output_dir}")

    # 创建session并使用自定义适配器
    session = requests.Session()
    adapter = CustomHttpAdapter()
    session.mount('https://', adapter)

    # 扑克牌花色和点数
    suits = ["hearts", "diamonds", "clubs", "spades"]
    ranks = ["2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"]

    base_url = "https://deckofcardsapi.com/static/img/"

    # 下载所有扑克牌图片
    for suit in suits:
        for rank in ranks:
            # DeckOfCards API使用的文件名格式
            if rank == "10":
                card_code = "0"
            elif rank in ["jack", "queen", "king", "ace"]:
                card_code = rank[0].upper()
            else:
                card_code = rank
                
            suit_code = suit[0].upper()
            filename = f"{card_code}{suit_code}.png"
            url = f"{base_url}{filename}"
            output_path = os.path.join(output_dir, f"{rank}_of_{suit}.png")
            
            print(f"下载图片: {filename}")
            try:
                response = session.get(url)
                response.raise_for_status()
                
                with open(output_path, 'wb') as f:
                    f.write(response.content)
                print(f"已保存: {output_path}")
                
            except Exception as e:
                print(f"下载失败 ({filename}): {e}")

    # 下载背面图片
    try:
        back_url = f"{base_url}back.png"
        back_path = os.path.join(output_dir, "back.png")
        response = session.get(back_url)
        response.raise_for_status()
        
        with open(back_path, 'wb') as f:
            f.write(response.content)
        print(f"已保存背面图片: {back_path}")
        
    except Exception as e:
        print(f"下载背面图片失败: {e}")

    print("扑克牌图片下载完成！")

if __name__ == "__main__":
    main() 