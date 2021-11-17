package com.application.dating.main.fragment


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
import com.application.dating.api.Dating_App_API

import com.application.dating.main.CardStackAdapter
import com.application.dating.model.Account
import com.application.dating.model.File_Image_Male
import com.yuyakaido.android.cardstackview.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.ArrayList
import com.application.dating.R
import com.application.dating.api.ServiceBuilder
import kotlinx.android.synthetic.main.main_tags_fragment.*

class Main_Tags_Fragment : Fragment() {
    private val TAG = "MainActivity"
    private var manager: CardStackLayoutManager? = null
    private var adapter: CardStackAdapter? = null
    lateinit var iMyAPI : Dating_App_API
    private var cardStackView: CardStackView? = null
    private var list: List<File_Image_Male> = ArrayList()
    private val conpositeDisposable = CompositeDisposable()
    private var progress : ProgressBar ?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.main_tags_fragment, container, false)
        progress = view.findViewById(R.id.progress)
        cardStackView = view.findViewById(R.id.card_stack_view);
        iMyAPI = ServiceBuilder.getInstance().create(Dating_App_API::class.java)
        manager = CardStackLayoutManager(requireActivity(), object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
            }

            override fun onCardSwiped(direction: Direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager!!.topPosition + " d=" + direction)
                if (direction == Direction.Right) {
                    Toast.makeText(requireActivity(), "LIKE", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Top) {
                    Toast.makeText(requireActivity(), "LIKE", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Left) {
                    Toast.makeText(requireActivity(), "NOPE", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Bottom) {
                    Toast.makeText(requireActivity(), "NOPE", Toast.LENGTH_SHORT).show()
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
        fetchData()
        cardStackView?.itemAnimator = DefaultItemAnimator()
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
/*        conpositeDisposable.add(api.getGetImageMale()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File_Image_Male>>() {
                    @Override
                    public void accept(List<File_Image_Male> file_image_males) throws Exception {
                        displayData(file_image_males);
                    }
                }));*/
        val acc = Account(
            id = 8,
            name = "Nguyễn Mai Trường Phát",
            dateofbirth = "04/11/2000",
            username = "nguyenmaitruongphat@gmail.com",
            password = "phat123456",
            gender = "nam",
            latitude = 10.253966700000005F,
            longitude = 105.9705111F,
            gender_requirement = "nữ",
            radius = 50,
            age_range = 0,
            live_at = "null",
            is_status = false
        )
        conpositeDisposable.addAll(
            iMyAPI.getImageMale(acc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({s->
                    displayData(s)
                    Toast.makeText(requireActivity(),"thành công",Toast.LENGTH_SHORT).show()
                },{t :Throwable? ->
                    Toast.makeText(requireActivity(),t!!.message,Toast.LENGTH_SHORT).show()
                }))
    }
    private fun displayData(fim: List<File_Image_Male>) {
        adapter = CardStackAdapter(requireActivity(), fim)
        cardStackView!!.layoutManager = manager
        cardStackView!!.adapter = adapter
        progress!!.visibility = View.GONE
        list = fim
    }
    override fun onStop() {
        conpositeDisposable.clear()
        super.onStop()
    }
}