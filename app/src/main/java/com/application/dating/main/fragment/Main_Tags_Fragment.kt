package com.application.dating.main.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast


import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.application.dating.*
import com.application.dating.R
import com.application.dating.api.Dating_App_API

import com.application.dating.main.CardStackAdapter
import com.application.dating.model.Taikhoan
import com.yuyakaido.android.cardstackview.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import com.application.dating.api.ServiceBuilder
import com.application.dating.model.Profile
import com.application.dating.model.Taikhoan_yeuthich
import kotlinx.android.synthetic.main.main_tags_fragment.view.*

class Main_Tags_Fragment : Fragment() {
    private val TAG = "MainActivity"
    private var manager: CardStackLayoutManager? = null
    private var adapter: CardStackAdapter? = null
    lateinit var iMyAPI : Dating_App_API
    private var cardStackView: CardStackView? = null
    private var list: List<Profile> = ArrayList()
    private val conpositeDisposable = CompositeDisposable()
    private var progress : ProgressBar ?= null
    companion object{
        private var pro : Profile ?= null
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.main_tags_fragment, container, false)
        progress = view.findViewById(R.id.progress)
        cardStackView = view.findViewById(R.id.card_stack_view);
        view.btn_start.setOnClickListener {
            if(MainActivity.is_xacminh!!){
                val intent = Intent(requireContext(), Activity_Location::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(requireContext(),"Vui lòng xác minh tài khoản",Toast.LENGTH_SHORT).show()
            }
        }
        view.btn_stop.setOnClickListener{
        }
        if(MainActivity.is_trangthai!!){
            manager = CardStackLayoutManager(requireActivity(), object : CardStackListener {
                override fun onCardDragging(direction: Direction, ratio: Float) {
                    Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
                }
                override fun onCardSwiped(direction: Direction) {
                    Log.d(TAG, "onCardSwiped: p=" + manager!!.topPosition + " d=" + direction)
                    if (direction == Direction.Right) {
                         val mat = Taikhoan_yeuthich(
                             id_nguoithich = MainActivity.id!!,
                             id_nguoiduocthich = pro!!.id,
                             is_yeuthich = true

                         )
                         conpositeDisposable.addAll(
                             iMyAPI.checkMatch(mat)
                                 .subscribeOn(Schedulers.io())
                                 .observeOn(AndroidSchedulers.mainThread())
                                 .subscribe ({s->
                                     if(s.contains("Bingo")){
                                         Toast.makeText(requireActivity(),"Hai bạn đã match nhau",Toast.LENGTH_SHORT).show()
                                     }
                                   /*  else if(s.contains("Create successfully")){
                                         Toast.makeText(requireActivity(),"Hai bạn không thích nhau",Toast.LENGTH_SHORT).show()
                                     }else{
                                         Toast.makeText(requireActivity(),"Đợi phản hồi từ đối phương",Toast.LENGTH_SHORT).show()
                                     }*/
                                 },{t :Throwable? ->
                                     Toast.makeText(requireActivity(),t!!.message,Toast.LENGTH_SHORT).show()
                                 }))
                       // Toast.makeText(requireActivity(),"Thích",Toast.LENGTH_SHORT).show()
                    }
                    if (direction == Direction.Top) {
                        val mat = Taikhoan_yeuthich(
                            id_nguoithich = MainActivity.id!!,
                            id_nguoiduocthich = pro!!.id,
                            is_yeuthich = true
                        )
                        conpositeDisposable.addAll(iMyAPI.checkMatch(mat)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe ({s->
                                                    if(s.contains("Bingo")){
                                                        Toast.makeText(requireActivity(),"Hai bạn đã match nhau",Toast.LENGTH_SHORT).show()
                                                    }
                                               /*     else if(s.contains("Create successfully")){
                                                        Toast.makeText(requireActivity(),"Hai bạn không thích nhau",Toast.LENGTH_SHORT).show()
                                                    }else{
                                                        Toast.makeText(requireActivity(),"Đợi phản hồi từ đối phương",Toast.LENGTH_SHORT).show()
                                                    }*/

                                                },{t :Throwable? ->
                                                    Toast.makeText(requireActivity(),t!!.message,Toast.LENGTH_SHORT).show()
                                                }))
                       // Toast.makeText(requireActivity(),"Thích",Toast.LENGTH_SHORT).show()
                    }
                    if (direction == Direction.Left) {
                        val mat = Taikhoan_yeuthich(
                            id_nguoithich = MainActivity.id!!,
                            id_nguoiduocthich = pro!!.id,
                            is_yeuthich = false
                        )
                            conpositeDisposable.addAll(
                                iMyAPI.checkMatch(mat)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe ({s->
                                       /* if(s.contains("Bingo")){
                                            Toast.makeText(requireActivity(),"Hai bạn đã match nhau",Toast.LENGTH_SHORT).show()
                                        }else if(s.contains("Create successfully")){
                                            Toast.makeText(requireActivity(),"Hai bạn không thích nhau",Toast.LENGTH_SHORT).show()
                                        }else{
                                            Toast.makeText(requireActivity(),"Đợi phản hồi từ đối phương",Toast.LENGTH_SHORT).show()
                                        }
                                        */
                                    },{t :Throwable? ->
                                        Toast.makeText(requireActivity(),t!!.message,Toast.LENGTH_SHORT).show()
                                    }))
                        Toast.makeText(requireActivity(),"Không thích",Toast.LENGTH_SHORT).show()
                    }
                    if (direction == Direction.Bottom) {
                        val mat = Taikhoan_yeuthich(
                            id_nguoithich = MainActivity.id!!,
                            id_nguoiduocthich = pro!!.id,
                            is_yeuthich = false
                        )
                        conpositeDisposable.addAll(
                               iMyAPI.checkMatch(mat)
                                   .subscribeOn(Schedulers.io())
                                   .observeOn(AndroidSchedulers.mainThread())
                                   .subscribe ({s->
   /*                                    if(s.contains("Bingo")){
                                           Toast.makeText(requireActivity(),"Hai bạn đã match nhau",Toast.LENGTH_SHORT).show()
                                       }else if(s.contains("Create successfully")){
                                           Toast.makeText(requireActivity(),"Hai bạn không thích nhau",Toast.LENGTH_SHORT).show()
                                       }else{
                                           Toast.makeText(requireActivity(),"Đợi phản hồi từ đối phương",Toast.LENGTH_SHORT).show()
                                       }*/
                                   },{t :Throwable? ->
                                       Toast.makeText(requireActivity(),t!!.message,Toast.LENGTH_SHORT).show()
                                   }))
                        Toast.makeText(requireActivity(),"Không thích",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCardRewound() {
                    Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
                }
                override fun onCardCanceled() {
                    Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
                }
                override fun onCardAppeared(view: View, position: Int) {
                    val tv = view.findViewById<TextView>(R.id.item_name)
                    Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)
                    pro  = list.get(position)
                }
                override fun onCardDisappeared(view: View, position: Int) {
                    val tv = view.findViewById<TextView>(R.id.item_name)
                    Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)

                }
            })
            // Paginating
/*        if (manager.getTopPosition() == adapter.getItemCount() - list.size()){
            progressBar.setVisibility(View.VISIBLE);
        }*/
            manager!!.setStackFrom(StackFrom.None)
            manager!!.setVisibleCount(3)
            manager!!.setTranslationInterval(8.0f)
            manager!!.setScaleInterval(0.95f)
            manager!!.setSwipeThreshold(0.3f)
            manager!!.setMaxDegree(20.0f)
            manager!!.setDirections(Direction.FREEDOM)
            manager!!.setCanScrollHorizontal(true)
            manager!!.setSwipeableMethod(SwipeableMethod.Manual)
            manager!!.setOverlayInterpolator(LinearInterpolator())
            cardStackView?.itemAnimator = DefaultItemAnimator()
        }
        return view
    }
/*    private void paginate() {
        List<File_Image_Male> old = adapter.getItems();
        List<ItemModel> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }*/

    /*    private void paginate() {
        List<File_Image_Male> old = adapter.getItems();
        List<ItemModel> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }*/
    private fun fetchData() {
/*        conpositeDisposable.add(iMyAPI.info_account()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File_Image_Male>>() {
                    @Override
                    public void accept(List<File_Image_Male> file_image_males) throws Exception {
                        displayData(file_image_males);
                    }
                }));*/
        val acc = Taikhoan(
            id = MainActivity.id!!,
            gioitinh = MainActivity.gioitinh,
            vido = Activity_Setting.vido,
            kinhdo = Activity_Setting.kinhdo,
            bankinh = Activity_Setting.endpoint_radius,
            dotuoitoithieu_yeuthich = Activity_Setting.endpoint_agemin,
            dotuoitoida_yeuthich = Activity_Setting.endpoint_agemax,
            is_trangthai = true,
            gioitinhyeuthich = Activity_Setting.gender_requirement
        )
            conpositeDisposable.addAll(
                iMyAPI.info_account(acc)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({s->
                        displayData(s)
                        Toast.makeText(requireActivity(),"thành công",Toast.LENGTH_SHORT).show()
                    },{t :Throwable? ->
                        Toast.makeText(requireActivity(),t!!.message,Toast.LENGTH_SHORT).show()
                    }))
    }
    private fun displayData(fim: List<Profile>) {
        adapter = CardStackAdapter(requireActivity(), fim)
        cardStackView!!.layoutManager = manager
        cardStackView!!.adapter = adapter
        progress!!.visibility = View.GONE
        list = fim
    }

    override fun onResume() {
        super.onResume()
        iMyAPI = ServiceBuilder.getInstance().create(Dating_App_API::class.java)
        fetchData()
    }
    override fun onStop() {
        conpositeDisposable.clear()
        super.onStop()
    }
}