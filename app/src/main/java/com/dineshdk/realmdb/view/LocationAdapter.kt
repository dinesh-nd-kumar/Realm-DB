package com.dineshdk.realmdb.view


import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dineshdk.realmdb.Model.UserLocation
import com.dineshdk.realmdb.databinding.RowItemBinding
import java.text.SimpleDateFormat
import java.util.Date

class LocationAdapter(val clickListener: ItemClickListener, var locationList: List<UserLocation>?, val geocoder: Geocoder)
    : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            RowItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,false))
    }

    override fun getItemCount(): Int {

        if (locationList == null)
            return 0
        return locationList!!.size

    }


    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        locationList?.get(position)?.let { holder.bindData(it) }

    }


    inner class LocationViewHolder(val binding:RowItemBinding):ViewHolder(binding.root) {
        val formatter = SimpleDateFormat("dd/MM hh:mm");
        fun bindData(loca : UserLocation){
                val add = geocoder.getFromLocation(loca.lat?:0.0,loca.lon?:0.0,1)
            binding.apply {
//                name.text = "${loca?.lat} ${loca?.lon}"
                if (add?.size!! > 0){
                    name.text = add[0]!!.locality
                }else
                    name.text = "${loca?.lat} ${loca?.lon}"


                val dateString = formatter.format(Date(loca.time?:0));
                tvAddress.text = dateString
                index.text = "$adapterPosition. ${loca.userId}"
                root.setOnClickListener{
                    clickListener.onItemClick(adapterPosition)
                }
            }
        }

    }
    public interface ItemClickListener{
        public fun onItemClick(position:Int)
    }
}