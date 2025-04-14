from PIL import Image, ImageDraw, ImageFont
import os

def get_suit_symbol(suit):
    symbols = {
        'hearts': '♥',
        'diamonds': '♦',
        'clubs': '♣',
        'spades': '♠'
    }
    return symbols.get(suit, '')

def draw_decorative_corners(draw, width, height, color):
    # 绘制装饰性的角落花纹
    corner_size = 30
    # 左上角
    draw.arc([5, 5, corner_size, corner_size], 180, 270, fill=color, width=2)
    # 右上角
    draw.arc([width-corner_size-5, 5, width-5, corner_size], 270, 0, fill=color, width=2)
    # 左下角
    draw.arc([5, height-corner_size-5, corner_size, height-5], 90, 180, fill=color, width=2)
    # 右下角
    draw.arc([width-corner_size-5, height-corner_size-5, width-5, height-5], 0, 90, fill=color, width=2)

def draw_royal_pattern(draw, width, height, color, rank):
    patterns = {
        'jack': '♞',    # 骑士
        'queen': '♛',   # 皇后
        'king': '♔'     # 国王
    }
    symbol = patterns.get(rank, '')
    if symbol:
        # 使用更大的字体绘制皇室符号
        font_size = 100
        font = ImageFont.truetype('Arial', font_size)
        # 在中心绘制皇室符号
        bbox = font.getbbox(symbol)
        w = bbox[2] - bbox[0]
        h = bbox[3] - bbox[1]
        draw.text((width//2-w//2, height//2-h//2), symbol, fill=color, font=font)
        
        # 添加装饰性的小皇冠
        crown = '♔'
        small_font = ImageFont.truetype('Arial', 30)
        draw.text((width//2-15, 50), crown, fill=color, font=small_font)

def create_card(rank, suit, output_path):
    # 创建空白图片
    width = 200
    height = 280
    image = Image.new('RGB', (width, height), 'white')
    draw = ImageDraw.Draw(image)
    
    # 设置颜色
    color = 'red' if suit in ['hearts', 'diamonds'] else 'black'
    
    # 绘制边框和装饰性角落
    draw.rectangle([(10, 10), (width-10, height-10)], outline=color, width=2)
    draw_decorative_corners(draw, width, height, color)
    
    # 获取花色符号
    suit_symbol = get_suit_symbol(suit)
    
    # 在四角绘制点数和花色，使用较小的字体
    corner_font = ImageFont.truetype('Arial', 20)
    corner_text = f"{rank}\n{suit_symbol}"
    
    # 调整四角文字位置
    draw.text((15, 15), corner_text, fill=color, font=corner_font, align='left')      # 左上角
    draw.text((width-35, 15), corner_text, fill=color, font=corner_font, align='right')  # 右上角
    draw.text((15, height-55), corner_text, fill=color, font=corner_font, align='left')  # 左下角
    draw.text((width-35, height-55), corner_text, fill=color, font=corner_font, align='right')  # 右下角
    
    # 对于普通数字牌，在中心绘制大号花色符号
    if rank not in ['jack', 'queen', 'king']:
        center_font = ImageFont.truetype('Arial', 80)
        bbox = center_font.getbbox(suit_symbol)
        w = bbox[2] - bbox[0]
        h = bbox[3] - bbox[1]
        
        # 如果是数字牌，添加对称的花色符号
        if rank.isdigit():
            num = int(rank)
            small_symbol_font = ImageFont.truetype('Arial', 30)
            positions = []
            
            if num == 1:
                # A只显示中心的大号花色符号
                draw.text((width//2-w//2, height//2-h//2), suit_symbol, fill=color, font=center_font)
            else:
                # 2-10的布局
                if num == 2:
                    positions = [(width//4, height//4), (3*width//4, 3*height//4)]
                elif num == 3:
                    positions = [(width//4, height//4), (width//2, height//2), (3*width//4, 3*height//4)]
                elif num == 4:
                    positions = [(width//4, height//4), (3*width//4, height//4),
                               (width//4, 3*height//4), (3*width//4, 3*height//4)]
                else:
                    # 5-10的布局
                    row_count = (num + 1) // 2
                    for i in range(num):
                        row = i // 2
                        col = i % 2
                        x = width//4 + col * width//2
                        y = height//(row_count+1) * (row + 1)
                        positions.append((x, y))
                
                # 绘制所有花色符号
                for pos in positions:
                    draw.text(pos, suit_symbol, fill=color, font=small_symbol_font, anchor="mm")
        else:
            # 对于A，只显示中心的大号花色符号
            draw.text((width//2-w//2, height//2-h//2), suit_symbol, fill=color, font=center_font)
    else:
        # 为J、Q、K绘制特殊的皇室图案
        draw_royal_pattern(draw, width, height, color, rank)
    
    # 保存图片
    image.save(output_path)

def create_card_back(output_path):
    # 创建空白图片
    width = 200
    height = 280
    image = Image.new('RGB', (width, height), 'navy')
    draw = ImageDraw.Draw(image)
    
    # 绘制边框
    draw.rectangle([(10, 10), (width-10, height-10)], outline='gold', width=2)
    
    # 绘制中心装饰图案
    center_pattern = '★'
    font = ImageFont.truetype('Arial', 60)
    bbox = font.getbbox(center_pattern)
    w = bbox[2] - bbox[0]
    h = bbox[3] - bbox[1]
    draw.text((width//2-w//2, height//2-h//2), center_pattern, fill='gold', font=font)
    
    # 绘制花纹图案
    for i in range(20, width-20, 20):
        for j in range(20, height-20, 20):
            if (i-20)//20 % 2 == (j-20)//20 % 2:  # 创建棋盘格效果
                draw.rectangle([(i, j), (i+10, j+10)], outline='gold', width=1)
    
    # 保存图片
    image.save(output_path)

def main():
    # 使用绝对路径
    current_dir = os.path.dirname(os.path.abspath(__file__))
    output_dir = os.path.join(current_dir, "src/main/resources/static/images/cards")
    
    try:
        os.makedirs(output_dir, exist_ok=True)
        print(f"创建目录: {output_dir}")
    except Exception as e:
        print(f"创建目录失败: {e}")
        return

    # 扑克牌花色和点数
    suits = ["hearts", "diamonds", "clubs", "spades"]
    ranks = ["2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"]

    # 生成所有扑克牌图片
    for suit in suits:
        for rank in ranks:
            output_path = os.path.join(output_dir, f"{rank}_of_{suit}.png")
            try:
                create_card(rank, suit, output_path)
                print(f"已生成: {output_path}")
            except Exception as e:
                print(f"生成失败 ({rank} of {suit}): {e}")

    # 生成背面图片
    try:
        back_path = os.path.join(output_dir, "back.png")
        create_card_back(back_path)
        print(f"已生成背面图片: {back_path}")
    except Exception as e:
        print(f"生成背面图片失败: {e}")

    print("扑克牌图片生成完成！")

if __name__ == "__main__":
    main() 