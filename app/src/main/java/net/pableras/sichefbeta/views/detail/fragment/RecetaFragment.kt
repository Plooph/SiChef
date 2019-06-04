package net.pableras.sichefbeta.views.detail.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.model.Receta
import org.jetbrains.anko.support.v4.toast
import net.pableras.sichefbeta.databinding.FragmentRecetaBinding

/**
 * A simple [Fragment] subclass.
 * Use the [RecetaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecetaFragment : Fragment() {

    private lateinit var data: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRecetaBinding  = DataBindingUtil.inflate(inflater, R.layout.fragment_receta, container, false)
        val rootview = binding.root

        if (this.arguments != null){
            data = this.arguments!!
            Log.d("elBundledelfragment", data.toString())
        }else{
            Log.d("elBundledelfragment", "se está creando de nuevo no se porque")
        }

        val receta = data.getSerializable("receta") as Receta
        Log.d("elBundle", receta.toString())

        binding.receta = receta

        return rootview
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "receta"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(receta: Receta): RecetaFragment {
            return RecetaFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SECTION_NUMBER, receta)
                }
            }
        }
    }
}
