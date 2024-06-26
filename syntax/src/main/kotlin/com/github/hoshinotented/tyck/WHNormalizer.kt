package com.github.hoshinotented.tyck

import com.github.hoshinotented.resolve.FreeBinding
import com.github.hoshinotented.syntax.core.*
import com.github.hoshinotented.syntax.core.Term.Companion.map
import kala.collection.immutable.ImmutableMap

object WHNormalizer : (Term) -> Term {
  fun whnf(term: Term): Term {
    return this(term)
  }
  
  override fun invoke(term: Term): Term {
    if (term is StableWHNF) return term
    val postTerm = term.map(this)
    
    return when (postTerm) {
      is StableWHNF -> throw IllegalStateException("unreachable")
      is FreeRefTerm -> postTerm
      is AppTerm -> {
        val f = postTerm.f
        val a = postTerm.a
        
        if (f is LamTerm) {
          invoke(f.body.instantiate(a))
        } else postTerm
      }
      
      is LetTerm -> {
        TODO()
      }
      
      is BoundRefTerm -> postTerm
    }
  }
}