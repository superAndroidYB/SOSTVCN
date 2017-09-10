package com.sostvcn.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.SosBookContent;
import com.umeng.socialize.media.Base;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookContentActivity extends BaseActivity {

    private int contentId;
    private SosBookContent content;
    private BookPageApi api;

    @ViewInject(R.id.textaaaa)
    private TextView textaaaa;

    private String s = "<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">约翰与他的众弟兄迥然有别，被称为<span>“<\\/span>耶稣所爱的门徒。<span>”<\\/span>他的性格丝毫不胆怯、软弱、或动摇，他具有和蔼可亲的本性，和一颗温暖爱人的心。他似乎比别人更多地享有与基督相交的友情，他也得着救主许多信任与喜爱的证据。他也是特蒙许可看见基督在山上变像的荣耀，和在客西马尼的痛苦那三位门徒中之一。而且我们的主在十字架上，最后受苦的时间，将他的母亲交给约翰照顾。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　救主对他<span>“<\\/span>所爱的门徒<span>”<\\/span>的爱，约翰以热烈忠诚，全心全力的爱报答了他的主。约翰紧靠着基督，犹如葡萄藤紧绕着挺立的柱子一样。为他的救主他冒着审判庭的危险，也在十字架前停留，而在听见基督已经复活的消息时，他赶紧的跑到坟墓那里去，并且本着他的热心，比性急的彼得更先赶到。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　约翰对于他主的爱，并非仅属人间友情的爱，乃是一位悔改的罪人，感觉已蒙基督宝血所救赎的爱。他感觉为他的主服务效劳，并受痛苦，是最大的尊荣。他爱耶稣的心，使他爱及凡基督为之替死的人。他的宗教具有实际性。他推理说，爱上帝的心，就必显示于爱上帝的儿女们。我们一再地听见他说<span>:<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">亲爱的弟兄啊，上帝既是这样爱我们，我们也当彼此相爱。<span>”<\\/span>【约壹<span>4:11<\\/span>】<span> “<\\/span>我们爱，因为上帝先爱我们。人若说，‘我爱上帝’，却恨他的弟兄，就是说谎话的；不爱他所能看见的弟兄，就不能爱没有看见的上帝。<span>” <\\/span>【<span>19<\\/span>，<span>20<\\/span>节】<\\/span><span style=\\\"line-height: 150%; font-family: 宋体;\\\">使徒的人生与他的教训相符合。在他心中所怀存对基督的热爱，促使他尽心尽力为他的同胞辛劳服务，尤其是在基督教会中的弟兄们。他是一位大有能力的传道士，热心与极度的诚恳，并且他所讲的话，带有使人信服的力量。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"center\\\" style=\\\"text-align: center; line-height: 150%;\\\"><b><span style=\\\"line-height: 150%; font-family: 宋体;\\\">由于恩典成为新造的人<\\/span><\\/b><span style=\\\"line-height: 150%; font-family: 宋体;\\\"><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　约翰的人生与品格，所显示的信赖的爱和不自私的虔诚，对今日基督的教会有无价之宝的教训。或有人认为约翰有这样的爱心，并不赖乎神圣的恩典，但就本性而言，约翰的品格，有严重的毛病，他骄傲自大，野心勃勃，对轻视与损害极其敏感易怒。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　约翰对他主的深爱与热情，并非基督爱他的原因，而是基督爱他的结果。约翰切望要变成耶稣的样式，而在基督之爱的更新变化力量之下，他便变为心里柔和谦卑，自我与基督有了联合的必然结果。这也是真正的成圣。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　人或许在品格上有显著的缺点，然而当他得以成为耶稣真诚的门徒时，神圣恩典的能力，使他成为新造的人。基督的爱使他改变过来，成为圣洁。但若有人自称为基督徒，而他的宗教没有在人生各方面的关系上，使他成为更良善的男女－－在性情与品格上，作活生生的基督的代表－－他们就根本上不是属于他的。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"center\\\" style=\\\"text-align: center; line-height: 150%;\\\"><b><span style=\\\"line-height: 150%; font-family: 宋体;\\\">建立品格的教训<\\/span><\\/b><span style=\\\"line-height: 150%; font-family: 宋体;\\\"><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　有一次，约翰和他的几位弟兄辩论<span>:<\\/span>他们中间谁应算为最大的。他们无意让所说的话，进入他们夫子的耳中。但耶稣洞悉他们的心意，就利用机会给予他的众门徒有关谦卑的教训。这教训并非只为当时听他说话的少数人，乃被记录下来，作为他直到末日一切门徒的益处。<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">耶稣坐下，叫十二个门徒来，说：‘若有人愿意作首先的，他必作众人末后的，作众人的用人。’<span>”<\\/span>【可<span>9:35<\\/span>】<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　凡心里有基督之灵的人，必没有野心要占有他们弟兄以上的地位。那在自己的眼中自视为小的人，必在上帝眼中被看为大的。<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">于是领过一个小孩子来，叫他站在门徒中间，又抱起他来，对他们说：‘凡为我名接待一个像这小孩子的，就是接待我；凡接待我的，不是接待我，乃是接待那差我来的。’<span>”<\\/span>【可<span>9:36<\\/span>，<span>37<\\/span>节】<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　这对于基督的众门徒，是何等宝贵的教训啊！忽视不注意自己道路上的人生义务，忽略怜悯与亲切，礼貌与仁爱，就连对待一个小孩子也是如此，便是忽略基督。约翰感悟到这教训的重要性，并因而获益。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　还有一次，他的兄弟雅各和自己，看见有一个人奉耶稣的名赶鬼，而因他没有立时参与他们的团体，他们便断定那人没有权柄行这样的神迹，于是不许他再这样作。本着他诚实的心意，约翰将这些情形告诉他的主。耶稣说<span>:<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">不要禁止他，因为没有人奉我的名行异能，反倒轻易毁谤我。不敌挡我们的，就是帮助我们的。<span>”<\\/span>【 <span>39<\\/span>，<span>40<\\/span>节】<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　还有，雅各与约翰由他们的母亲，向耶稣要求准许他们在基督的国里，占最高尊荣的职位。救主回答说<span>:<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">你们不知道所求的是什么。<span>”<\\/span>【可<span>10:38<\\/span>】<\\/span><span style=\\\"line-height: 150%; font-family: 宋体;\\\"> 我们许多人对于自己祷告的实意，知道得何其少啊！耶稣晓得要购买该种荣耀，所必须付上的无限量牺牲的代价，当他<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">因那摆在前面的喜乐，就轻看羞辱，忍受了十字架的苦难。<span>”<\\/span>【来<span>12:2<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 宋体;\\\">】 所享受的喜乐，乃是因他所受的羞辱，他的痛苦，和他所流的血，得以看见生灵得救了。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　这乃是基督将来要享的荣耀，也是这两位门徒要求得蒙允许分享的。耶稣问他们<span>:<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">‘我所喝的杯你们能喝吗？我所受的洗你们能受吗<span>?<\\/span>’他们说：‘我们能。’<span>”<\\/span>【可<span>10:38<\\/span>，<span>39<\\/span>】<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　他们对那种洗所代表的意义，了解得何其少啊！耶稣说<span>:<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">我所喝的杯，你们也要喝；我所受的洗，你们也要受。只是坐在我的左右，不是我可以赐的，乃是为谁预备的，就赐给谁。<span>”<\\/span>【可<span>10:39<\\/span>，<span>40<\\/span>节】<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"center\\\" style=\\\"text-align: center; line-height: 150%;\\\"><b><span style=\\\"line-height: 150%; font-family: 宋体;\\\">责备骄傲与野心<\\/span><\\/b><span style=\\\"line-height: 150%; font-family: 宋体;\\\"><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　耶稣洞悉引起这要求的动机，就用下面的话，责备门徒的骄傲与野心<span>:<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">你们知道，外邦人有尊为君王的，治理他们，有大臣操权管束他们。只是在你们中间，不是这样。你们中间，谁愿为大，就必作你们的用人；在你们中间，谁愿为首，就必作众人的仆人。因为人子来，并不是要受人的服侍，乃是要服侍人，并且要舍命，作多人的赎价。<span>”<\\/span>【 <span>42<\\/span>－<span>45<\\/span>节】<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　有一次，基督差遣传信者到撒玛利亚人的一个乡村；要求那里的民众，为他和他的门徒预备食物。当救主走近那村子时，看来他打算经过他们的村子往耶路撒冷去。这激起了撒玛利亚人的仇恨，以至他们非但没有差使者邀请，并恳求他在他们中间停留，也没有向他表示，他们会给予对一个普通旅客的礼貌和款待。耶稣从来不勉强任何人接待他，而撒玛利亚人损失了，他们本来可以得着的福惠，只要他们邀请他作他们的宾客。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　我们或不明白，这样不礼貌的对待天上至尊的行动，但我们这自称为基督的门徒者，多么频繁地犯了同样的忽略。我们是否恳求耶稣在我们的心里，或我们的家中居留下来呢<span>?<\\/span>他胸怀充满仁爱、恩典与福惠，也随时准备将这种种的福气赐给我们；但我们常常未得享这些福惠，而仍然似乎心满意足，像撒玛利亚人一样。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　门徒原晓得基督有意要以他的临格，赐福给撒玛利亚人，而当他们看见当地居民向他们的夫子所表示的冷酷、嫉妒、与不礼貌的态度时，他们便满心诧异和愤怒。雅各和约翰更为激动。他们所尊重的主，竟遭人如此的对待，在他们看来是那么大的恶行，决不可轻易放过，而不立时给予处罚。本乎他们的热心，他们就说<span>:<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">主啊，你要我们吩咐火从天上降下来烧灭他们，像以利亚所作的吗<span>?”<\\/span>【路<span>9:54<\\/span>】<\\/span><span style=\\\"line-height: 150%; font-family: 宋体;\\\">这话是指奉差遣捉拿先知以利亚的军官，和五十个兵士所遭遇的毁灭。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　耶稣责备他的门徒说<span>:<\\/span><\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">“<\\/span><span style=\\\"line-height: 150%; font-family: 楷体;\\\">你们的心如何，你们并不知道。人子来不是要灭人的性命，是要救人的性命。<span>”<\\/span>【 <span>55<\\/span>，<span>56<\\/span>节】<\\/span><span style=\\\"line-height: 150%; font-family: 宋体;\\\">约翰和与他同作门徒的，在有基督为教师的学校里受教。凡愿意看明自己的缺点，而决心改善他们品行的人，都有充足的机会。约翰重视每项的教训，也时时努力要使他自己的人生与神圣的楷模相似。耶稣的教训，说明温和、谦卑与仁爱，为在恩典上有长进，并适于担任他的工作所不可或缺的要素，对约翰有极大的价值。这些教训，也是赐给我们每一个人，和教会中所有的弟兄姐妹，犹如赐予早期基督的门徒一样。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"center\\\" style=\\\"text-align: center; line-height: 150%;\\\"><b><span style=\\\"line-height: 150%; font-family: 宋体;\\\">约翰与犹大<\\/span><\\/b><span style=\\\"line-height: 150%; font-family: 宋体;\\\"><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　约翰与犹大在品格上显著的区别，也给予我们深刻实用的教训。约翰原是成圣的活生生的例证。反过来说，犹大有敬虔的外貌，但他品格的性质却像撒但的样式过于像神圣的样式。他自称是基督的门徒，但在言语和行为上都否认基督。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　犹大像约翰一样，有同样宝贵的机会，可以学习并效法他的楷模。他也听了基督所给予的教训，而他的品格，有可能因神圣的恩惠而改变过来。可是当约翰诚挚地与他自己的缺点不住地作战，并力求变成基督的样式，犹大却违背自己的良心，屈从试探，养成要使他将来变成撒但模样的不诚实的恶习。<span><\\/span><\\/span><\\/p>\\r\\n<p align=\\\"left\\\" style=\\\"line-height: 150%;\\\"><span style=\\\"line-height: 150%; font-family: 宋体;\\\">　　这两位门徒代表基督教的世界。大众都自认为基督的门徒，可是当有一等以谦卑温柔行事为人，向耶稣学习，那另一等人显明他们是不行道的，只是听道的。有一等因真理得以成圣，而另一等对于神圣恩典的变化改造之能，却全无所知。前一等人日日向自我死，并胜过罪恶。后一等人乃是放纵自己的情欲，并成为撒但的奴仆。<span><\\/span><\\/span><\\/p>";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_content;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        contentId = getIntent().getIntExtra("contentId", 0);
        api = HttpUtils.getInstance(this).getRetofitClinet().builder(BookPageApi.class);
        api.loadBookContent(contentId).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseObjectResponse<SosBookContent>>(new SubscriberOnNextListener<BaseObjectResponse<SosBookContent>>() {
                    @Override
                    public void onNext(BaseObjectResponse<SosBookContent> sosBookContentBaseObjectResponse) {
                        content = sosBookContentBaseObjectResponse.getResults();
                        try {
                            Parser parser = new Parser(content.getContent_text());
                            NodeList nodeList = parser.parse(new NodeFilter() {
                                @Override
                                public boolean accept(Node node) {
                                    return true;
                                }
                            });

                            StringBuffer sb = new StringBuffer();
                            for (Node node : nodeList.toNodeArray()) {
                                sb.append(node.getText());
                            }
                            textaaaa.setText(sb.toString());

                        } catch (ParserException e) {
                            e.printStackTrace();
                        }
                    }
                }, this));
    }

    public static void start(Context context, int contentId) {
        Intent intent = new Intent(context, BookContentActivity.class);
        intent.putExtra("contentId", contentId);
        context.startActivity(intent);
    }


}
